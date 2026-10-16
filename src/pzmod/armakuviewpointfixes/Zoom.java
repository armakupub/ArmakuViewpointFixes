package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.core.View;
import viewpoint.input.FreeCam;
import viewpoint.input.Look;
import viewpoint.platform.LiveSettings;
import viewpoint.platform.SettingsWindow;
import zombie.characters.IsoPlayer;
import zombie.input.Mouse;

// Viewpoint frames by screen share, so its side and height offsets would grow with a zoomed distance.
public class Zoom {
    static final float EASE_TIME = 0.15f;
    static final float LEAVE_TIME = 0.4f;
    static final Field FARTHER, RIG, LIMIT;
    static float shown = Float.NaN;
    static boolean shownSeated;
    static long lastNanos;
    static volatile float factor = 1.0f;

    static {
        Field farther = null, rig = null, limit = null;
        try {
            Class<?> step = Class.forName("viewpoint.camera.CameraStep");
            farther = step.getDeclaredField("farther");
            farther.setAccessible(true);
            rig = step.getDeclaredField("rig");
            rig.setAccessible(true);
            limit = Class.forName("viewpoint.camera.CameraRig").getDeclaredField("limit");
            limit.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] no mouse wheel zoom, Viewpoint changed: " + e);
        }
        FARTHER = farther;
        RIG = rig;
        LIMIT = limit;
    }

    public static void mouseUpdated() {
        int wheel = Mouse.wheelDelta;
        if (wheel == 0 || FARTHER == null || !Settings.WHEEL_ZOOM.get()
                || !View.enabled || !View.thirdPerson || !Look.captured || FreeCam.active) return;
        IsoPlayer p = IsoPlayer.players[0];
        if (p == null) return;
        Mouse.wheelDelta = 0;
        boolean seated = p.getVehicle() != null;
        float a = (seated ? Settings.ZOOM_SEATED_NEAR : Settings.ZOOM_NEAR).get();
        float b = (seated ? Settings.ZOOM_SEATED_FAR : Settings.ZOOM_FAR).get();
        float near = Math.min(a, b), far = Math.max(a, b);
        if (far - near < 1.0e-3f) return;
        int steps = Math.round(Settings.ZOOM_STEPS.get());
        if (!seated && IndoorZoom.active()) {
            near = Math.min(near, Settings.INDOOR_ZOOM_FACTOR.get());
            IndoorZoom.zoom = step(IndoorZoom.zoom, near, far, steps, wheel);
            return;
        }
        LiveSettings.Number zoom = seated ? Settings.ZOOM_SEATED : Settings.ZOOM_ON_FOOT;
        zoom.set(step(zoom.get(), near, far, steps, wheel));
        SettingsWindow.edited();
    }

    public static void distance(Object step, boolean seated) {
        if (FARTHER == null) return;
        long now = System.nanoTime();
        float dt = lastNanos == 0L ? 0.0f : Math.min(0.1f, (now - lastNanos) * 1.0e-9f);
        lastNanos = now;
        IndoorZoom.update(IsoPlayer.players[0], seated, dt, now);
        float target = seated ? (Settings.WHEEL_ZOOM.get() ? Settings.ZOOM_SEATED.get() : 1.0f)
                : IndoorZoom.active() ? IndoorZoom.zoom : outdoors();
        if (Float.isNaN(shown) || seated != shownSeated) shown = target;
        shownSeated = seated;
        if (Math.abs(target - shown) < 0.01f) IndoorZoom.left = false;
        float was = shown;
        shown += (target - shown) * (1.0f - (float) Math.exp(-dt / (IndoorZoom.left ? LEAVE_TIME : EASE_TIME)));
        factor = shown;
        try {
            FARTHER.setFloat(step, FARTHER.getFloat(step) * shown);
            // Viewpoint holds a grown boom back as if a wall cleared; a wall in the way pulls it in at once.
            if (shown > was && was > 0.0f) {
                Object r = RIG.get(step);
                float l = LIMIT.getFloat(r);
                if (l < 1.0e6f) LIMIT.setFloat(r, l * shown / was);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            factor = 1.0f;
        }
    }

    static float outdoors() {
        return Settings.WHEEL_ZOOM.get() ? Settings.ZOOM_ON_FOOT.get() : 1.0f;
    }

    // Offsets are worked out from Viewpoint's own distance.
    public static float framed(float distance) {
        float f = factor;
        return f > 0.0f ? distance / f : distance;
    }

    // Log-spaced steps feel alike; settings read back rounded to their slider step.
    static float step(float z, float near, float far, int steps, int wheel) {
        steps = Math.max(1, steps);
        double size = Math.log(far / near) / steps;
        double at = Math.log(Math.max(near, Math.min(far, z)) / near) / size;
        int nearest = (int) Math.round(at);
        boolean onStep = Math.abs(near * Math.exp(nearest * size) - z) <= 0.01;
        int step = onStep ? nearest + (wheel > 0 ? -1 : 1) : (int) (wheel > 0 ? Math.floor(at) : Math.ceil(at));
        return (float) (near * Math.exp(Math.max(0, Math.min(steps, step)) * size));
    }
}
