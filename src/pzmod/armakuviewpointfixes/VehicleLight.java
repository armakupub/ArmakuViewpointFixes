package pzmod.armakuviewpointfixes;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import viewpoint.light.OwnedLight;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.vehicles.BaseVehicle;

// Viewpoint lights a vehicle by the square under its centre and caps lamp light there: it steps per square.
public class VehicleLight {
    static final float EASE = 0.15f;
    static final long FORGET_NANOS = 5_000_000_000L;

    static final class State {
        float r, g, b, sky;
        long nanos;
    }

    static final Map<BaseVehicle, State> states = new IdentityHashMap<>();
    public static boolean active;
    public static int packed;
    public static float skyShare;
    static long sweptNanos;

    // Main thread.
    public static void begin(BaseVehicle vehicle) {
        active = false;
        if (!Settings.VEHICLE_LIGHT.get() || vehicle == null) return;
        IsoCell cell = IsoWorld.instance == null ? null : IsoWorld.instance.getCell();
        if (cell == null) return;
        float x = vehicle.getX() - 0.5f, y = vehicle.getY() - 0.5f;
        int z = (int) Math.floor(vehicle.getZ());
        int x0 = (int) Math.floor(x), y0 = (int) Math.floor(y);
        float tx = x - x0, ty = y - y0;
        float r = 0.0f, g = 0.0f, b = 0.0f, s = 0.0f, weight = 0.0f;
        for (int i = 0; i < 4; i++) {
            int sx = x0 + (i & 1), sy = y0 + (i >> 1);
            float w = ((i & 1) == 0 ? 1.0f - tx : tx) * ((i >> 1) == 0 ? 1.0f - ty : ty);
            int light = OwnedLight.modelLightAt(sx, sy, z);
            IsoGridSquare sq = cell.getGridSquare(sx, sy, z);
            if (light < 0 || sq == null) continue;
            r += (light & 0xFF) * w;
            g += (light >> 8 & 0xFF) * w;
            b += (light >> 16 & 0xFF) * w;
            s += OwnedLight.modelSky(sx, sy, z, sq.isOutside() && !sq.haveRoof) * w;
            weight += w;
        }
        if (weight <= 1.0e-4f) return;
        r /= weight;
        g /= weight;
        b /= weight;
        s /= weight;

        long now = System.nanoTime();
        State st = states.get(vehicle);
        if (st == null || now - st.nanos > FORGET_NANOS) {
            if (st == null) states.put(vehicle, st = new State());
            st.r = r;
            st.g = g;
            st.b = b;
            st.sky = s;
        } else {
            float dt = Math.min(0.1f, (now - st.nanos) * 1.0e-9f);
            float k = 1.0f - (float) Math.exp(-dt / EASE);
            st.r += (r - st.r) * k;
            st.g += (g - st.g) * k;
            st.b += (b - st.b) * k;
            st.sky += (s - st.sky) * k;
        }
        st.nanos = now;
        packed = channel(st.r) | channel(st.g) << 8 | channel(st.b) << 16;
        skyShare = st.sky;
        active = true;
        sweep(now);
    }

    public static void end() {
        active = false;
    }

    static int channel(float v) {
        return Math.max(0, Math.min(255, Math.round(v)));
    }

    static void sweep(long now) {
        if (now - sweptNanos < FORGET_NANOS) return;
        sweptNanos = now;
        for (Iterator<State> it = states.values().iterator(); it.hasNext(); ) {
            if (now - it.next().nanos > FORGET_NANOS) it.remove();
        }
    }
}
