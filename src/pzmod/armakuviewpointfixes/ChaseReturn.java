package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.input.Look;
import viewpoint.platform.KeyInput;
import viewpoint.platform.Keys;

// Viewpoint swings the vehicle camera back after a fixed 0.75 s of resting mouse; a NaN heading holds it.
public class ChaseReturn {
    static final Field MOVED_NANOS;
    static boolean held;

    static {
        Field f = null;
        try {
            f = Look.class.getDeclaredField("movedNanos");
            f.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] free look in a vehicle off, Viewpoint changed: " + e);
        }
        MOVED_NANOS = f;
    }

    // Main thread.
    public static float heading(float yaw, boolean chase) {
        boolean down = !Float.isNaN(yaw) && Look.captured && MOVED_NANOS != null && Settings.SEATED_FREE_LOOK.get()
                && Keys.FREE_LOOK.down(KeyInput::physicallyDown);
        if (held && !down && !Float.isNaN(yaw)) release();
        held = down;
        if (Float.isNaN(yaw)) return yaw;
        if (down) return Float.NaN;
        if (chase && (System.nanoTime() - Look.movedNanos()) * 1.0e-9f <= Settings.CHASE_DELAY.get()) return Float.NaN;
        return yaw;
    }

    static void release() {
        try {
            MOVED_NANOS.setLong(null, System.nanoTime() - 60_000_000_000L);
        } catch (IllegalAccessException e) {
            System.out.println("[ArmakuViewpointFixes] free look release: " + e);
        }
    }
}
