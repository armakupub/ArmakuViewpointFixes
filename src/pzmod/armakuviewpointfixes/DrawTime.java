package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.joml.Vector3f;
import viewpoint.core.Frame;
import viewpoint.render.ModelDraws;
import viewpoint.render.SpotDraws;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.model.ModelSlotRenderData;
import zombie.vehicles.BaseVehicle;

// Viewpoint draws a frame a varying time after capturing it, so the vehicle and camera move on by that gap.
public class DrawTime {
    static final float MOST = 0.015f;
    static final float LATENCY_TIME = 1.0f;
    static final int VALUES = 68;
    static final int SPHERE = 24;
    static final int PREVIOUS = 36;
    static final int MOTION = 8;
    static final int ITEMS = 32;
    static final int LAMP_ITEMS = 8;
    // Kept per record, so a frame drawn twice is moved from the original.
    static final int KEPT = 19;
    static final Field RECORDS, KEYS, FLAGS, DRAWER_FRAME;
    static volatile boolean broken;

    static final class Item {
        int from, to, slot;
        float vx, vy, rate, cx, cz;
    }

    static final class Shot {
        long number = Long.MIN_VALUE;
        long started;
        float camVX, camVY;
        int count;
        final Item[] items = new Item[ITEMS];
        boolean saved;
        float[] kept = new float[64 * KEPT];
        int lampItems;
        final Item[] lamps = new Item[LAMP_ITEMS];
        boolean lampsSaved;
        final float[] keptLamps = new float[768];
        final float[] keptMaps = new float[55];
    }

    static final Map<Frame, Shot> shots = new ConcurrentHashMap<>();
    static final Vector3f velocity = new Vector3f();
    static volatile long frameStart;
    static Item open;

    static {
        Field records = null, keys = null, flags = null, frame = null;
        try {
            records = ModelDraws.class.getDeclaredField("values");
            keys = ModelDraws.class.getDeclaredField("keys");
            flags = ModelDraws.class.getDeclaredField("flags");
            frame = Class.forName("viewpoint.SceneDrawer").getDeclaredField("frame");
            records.setAccessible(true);
            keys.setAccessible(true);
            flags.setAccessible(true);
            frame.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            broken = true;
            System.out.println("[ArmakuViewpointFixes] vehicles drawn at capture time, Viewpoint changed: " + e);
        }
        RECORDS = records;
        KEYS = keys;
        FLAGS = flags;
        DRAWER_FRAME = frame;
    }

    // Physics is current as of the frame's start, not its capture.
    public static void frameStarted() {
        frameStart = System.nanoTime();
    }

    static Shot shot(Frame frame) {
        if (broken || frame.onCamera || !Settings.STEADY_CURVES.get()) return null;
        Shot s = shots.computeIfAbsent(frame, k -> new Shot());
        if (s.number != frame.number) {
            s.number = frame.number;
            s.started = frameStart;
            s.camVX = 0.0f;
            s.camVY = 0.0f;
            s.count = 0;
            s.saved = false;
            s.lampItems = 0;
            s.lampsSaved = false;
        }
        return s;
    }

    static Item start(Frame frame) {
        Shot s = shot(frame);
        if (s == null || s.count == ITEMS) return null;
        Item it = s.items[s.count];
        if (it == null) it = s.items[s.count] = new Item();
        s.count++;
        it.from = frame.scene.models.count();
        it.to = it.from;
        open = it;
        return it;
    }

    static void moving(Frame frame, Item it, BaseVehicle vehicle, int link) {
        VehicleTurn.track(vehicle, link);
        vehicle.getLinearVelocity(velocity);
        it.vx = velocity.x;
        it.vy = velocity.z;
        it.rate = VehicleTurn.rates[link].rate;
        if (link == 0) {
            Shot s = shots.get(frame);
            s.camVX = it.vx;
            s.camVY = it.vy;
        }
    }

    public static void beforeVehicle(Frame frame, BaseVehicle vehicle) {
        open = null;
        int link = VehicleTurn.link(vehicle);
        if (link < 0) return;
        Item it = start(frame);
        if (it == null) return;
        moving(frame, it, vehicle, link);
        it.cx = frame.camX - (float) VehicleTurn.centre[0];
        it.cz = frame.camY - (float) VehicleTurn.centre[1];
    }

    public static void beforeCharacter(Frame frame, ModelSlotRenderData data, IsoGameCharacter chr) {
        open = null;
        BaseVehicle vehicle = data.inVehicle ? chr.getVehicle() : null;
        int link = vehicle == null ? -1 : VehicleTurn.link(vehicle);
        if (link < 0) return;
        Item it = start(frame);
        if (it == null) return;
        moving(frame, it, vehicle, link);
        it.cx = frame.camX - (float) VehicleTurn.centre[0];
        it.cz = frame.camY - (float) VehicleTurn.centre[1];
    }

    static void lamps(Frame frame, BaseVehicle vehicle, int link, int from, int to, int slot, float cx, float cz) {
        if (to <= from && slot < 0) return;
        Shot s = shot(frame);
        if (s == null || s.lampItems == LAMP_ITEMS) return;
        Item it = s.lamps[s.lampItems];
        if (it == null) it = s.lamps[s.lampItems] = new Item();
        s.lampItems++;
        it.from = from;
        it.to = to;
        it.slot = slot;
        vehicle.getLinearVelocity(velocity);
        it.vx = velocity.x;
        it.vy = velocity.z;
        it.rate = VehicleTurn.rates[link].rate;
        it.cx = cx;
        it.cz = cz;
    }

    public static void after(Frame frame) {
        if (open != null) open.to = frame.scene.models.count();
        open = null;
    }

    // Render thread from here on.
    static Thread drawer;
    static Frame drawing;
    static long drawnAt;
    static float latency = Float.NaN;
    static float ahead, camDX, camDY;
    static Map<Object, float[]> drawn = new IdentityHashMap<>();
    static Map<Object, float[]> before = new IdentityHashMap<>();
    static final ArrayDeque<float[]> spare = new ArrayDeque<>();

    public static void draw(Object sceneDrawer) {
        long now = System.nanoTime();
        drawer = Thread.currentThread();
        ahead = 0.0f;
        camDX = 0.0f;
        camDY = 0.0f;
        Map<Object, float[]> t = before;
        before = drawn;
        drawn = t;
        for (float[] m : drawn.values()) spare.push(m);
        drawn.clear();
        if (broken) return;
        try {
            Frame frame = (Frame) DRAWER_FRAME.get(sceneDrawer);
            drawing = frame;
            BehindClock.drawing(frame);
            Shot s = shots.get(frame);
            if (s == null || s.number != frame.number || s.count + s.lampItems == 0 || s.started == 0L) {
                latency = Float.NaN;
                drawnAt = 0L;
                return;
            }
            float late = (now - s.started) / 1.0e9f;
            float dt = drawnAt == 0L ? 0.0f : Math.min(0.1f, (now - drawnAt) / 1.0e9f);
            drawnAt = now;
            latency = Float.isNaN(latency) ? late : latency + (late - latency) * (1.0f - (float) Math.exp(-dt / LATENCY_TIME));
            ahead = Math.max(-MOST, Math.min(MOST, late - latency));
            camDX = s.camVX * ahead;
            camDY = s.camVY * ahead;
            move(frame, s);
            moveLamps(frame, s);
        } catch (ReflectiveOperationException | RuntimeException e) {
            broken = true;
            ahead = 0.0f;
            camDX = 0.0f;
            camDY = 0.0f;
            System.out.println("[ArmakuViewpointFixes] vehicles drawn at capture time, Viewpoint changed: " + e);
        }
    }

    static boolean drawing(Frame frame) {
        return frame == drawing && Thread.currentThread() == drawer;
    }

    // Scene axes: x = camX - x, z = camY - y, y up.
    public static void eye(Frame frame, Object eye) {
        if (!drawing(frame)) return;
        float[] e = (float[]) eye;
        e[0] -= camDX;
        e[2] -= camDY;
    }

    static void moveLamps(Frame frame, Shot s) {
        if (s.lampItems == 0) return;
        SpotDraws spots = frame.scene.spots;
        int floats = Math.min(spots.lampCount * HeadlightLead.RECORD, spots.lamps.length);
        if (!s.lampsSaved) {
            System.arraycopy(spots.lamps, 0, s.keptLamps, 0, floats);
            System.arraycopy(spots.carMaps, 0, s.keptMaps, 0, s.keptMaps.length);
            s.lampsSaved = true;
        }
        System.arraycopy(s.keptLamps, 0, spots.lamps, 0, floats);
        System.arraycopy(s.keptMaps, 0, spots.carMaps, 0, s.keptMaps.length);
        for (int i = 0; i < s.lampItems; i++) {
            Item it = s.lamps[i];
            HeadlightLead.move(spots.lamps, spots.carMaps, it.from, it.to, it.slot, it.rate * ahead, it.cx, it.cz,
                    -it.vx * ahead, -it.vy * ahead);
        }
    }

    static void move(Frame frame, Shot s) throws ReflectiveOperationException {
        ModelDraws models = frame.scene.models;
        float[] values = (float[]) RECORDS.get(models);
        Object[] keys = (Object[]) KEYS.get(models);
        int[] flags = (int[]) FLAGS.get(models);
        int count = models.count();
        if (!s.saved) {
            int records = 0;
            for (int i = 0; i < s.count; i++) records += Math.max(0, Math.min(s.items[i].to, count) - s.items[i].from);
            if (s.kept.length < records * KEPT) s.kept = new float[records * KEPT * 2];
            int k = 0;
            for (int i = 0; i < s.count; i++) {
                Item it = s.items[i];
                for (int n = it.from; n < Math.min(it.to, count); n++, k += KEPT) {
                    System.arraycopy(values, n * VALUES, s.kept, k, 16);
                    System.arraycopy(values, n * VALUES + SPHERE, s.kept, k + 16, 3);
                }
            }
            s.saved = true;
        }
        int k = 0;
        for (int i = 0; i < s.count; i++) {
            Item it = s.items[i];
            float a = it.rate * ahead;
            float c = (float) Math.cos(a);
            float sn = (float) Math.sin(a);
            float ox = it.cx - (it.cx * c + it.cz * sn) - it.vx * ahead;
            float oz = it.cz - (-it.cx * sn + it.cz * c) - it.vy * ahead;
            for (int n = it.from; n < Math.min(it.to, count); n++, k += KEPT) {
                int r = n * VALUES;
                for (int col = 0; col < 16; col += 4) {
                    float x = s.kept[k + col];
                    float z = s.kept[k + col + 2];
                    float w = s.kept[k + col + 3];
                    values[r + col] = x * c + z * sn + ox * w;
                    values[r + col + 1] = s.kept[k + col + 1];
                    values[r + col + 2] = -x * sn + z * c + oz * w;
                    values[r + col + 3] = w;
                }
                float x = s.kept[k + 16];
                float z = s.kept[k + 18];
                values[r + SPHERE] = x * c + z * sn + ox;
                values[r + SPHERE + 2] = -x * sn + z * c + oz;
                Object key = keys[n];
                if (key == null) continue;
                // Motion is measured against the last frame as drawn, not as captured.
                float[] last = before.get(key);
                if (last != null && (flags[n] & MOTION) != 0) System.arraycopy(last, 0, values, r + PREVIOUS, 16);
                float[] m = spare.isEmpty() ? new float[16] : spare.pop();
                System.arraycopy(values, r, m, 0, 16);
                drawn.put(key, m);
            }
        }
    }
}
