package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.core.CameraShot;
import viewpoint.core.Frame;

// Viewpoint picks 3D corpses from the character, dropping those between it and the third-person camera.
public class CorpseReach {
    static final Field EYE_X, EYE_Y, EYE_UP, FORWARD_X, FORWARD_Y, FORWARD_UP, LEVEL_X, LEVEL_Y, APEX_X, APEX_Y;

    static {
        Field[] f = new Field[10];
        String[] names = {"eyeX", "eyeY", "eyeUp", "forwardX", "forwardY", "forwardUp", "levelX", "levelY", "apexX", "apexY"};
        try {
            Class<?> view = Class.forName("viewpoint.models.CorpseView");
            for (int i = 0; i < names.length; i++) {
                f[i] = view.getDeclaredField(names[i]);
                f[i].setAccessible(true);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            f[0] = null;
            System.out.println("[ArmakuViewpointFixes] corpses behind the character off, Viewpoint changed: " + e);
        }
        EYE_X = f[0];
        EYE_Y = f[1];
        EYE_UP = f[2];
        FORWARD_X = f[3];
        FORWARD_Y = f[4];
        FORWARD_UP = f[5];
        LEVEL_X = f[6];
        LEVEL_Y = f[7];
        APEX_X = f[8];
        APEX_Y = f[9];
    }

    // Main thread.
    public static void begin(Frame frame) {
        if (EYE_X == null || !Settings.CORPSE_REACH.get() || frame.onCamera) return;
        CameraShot shot = frame.cameraShot;
        if (!shot.on) return;
        float back = Math.max(0.0f, shot.distance + shot.addBack);
        if (back <= 0.0f) return;
        try {
            float fx = FORWARD_X.getFloat(null), fy = FORWARD_Y.getFloat(null), fu = FORWARD_UP.getFloat(null);
            EYE_X.setFloat(null, EYE_X.getFloat(null) - fx * back);
            EYE_Y.setFloat(null, EYE_Y.getFloat(null) - fy * back);
            EYE_UP.setFloat(null, EYE_UP.getFloat(null) - fu * back);
            float flat = (float) Math.sqrt(fx * fx + fy * fy) * back;
            APEX_X.setFloat(null, APEX_X.getFloat(null) - LEVEL_X.getFloat(null) * flat);
            APEX_Y.setFloat(null, APEX_Y.getFloat(null) - LEVEL_Y.getFloat(null) * flat);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
