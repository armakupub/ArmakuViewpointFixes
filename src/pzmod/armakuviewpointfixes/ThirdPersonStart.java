package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.platform.LiveSettings;

public class ThirdPersonStart {
    static final LiveSettings.Toggle ON_FOOT, SEATED;

    static {
        LiveSettings.Toggle onFoot = null, seated = null;
        try {
            Class<?> c = Class.forName("viewpoint.camera.CameraSettings");
            onFoot = toggle(c, "ON_FOOT");
            seated = toggle(c, "SEATED");
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] third person start off, Viewpoint changed: " + e);
            onFoot = null;
        }
        ON_FOOT = onFoot;
        SEATED = seated;
    }

    static LiveSettings.Toggle toggle(Class<?> c, String name) throws ReflectiveOperationException {
        Field f = c.getDeclaredField(name);
        f.setAccessible(true);
        return (LiveSettings.Toggle) f.get(null);
    }

    public static void switched(boolean on) {
        if (!on || ON_FOOT == null || !Settings.THIRD_PERSON_START.get()) return;
        ON_FOOT.set(true);
        SEATED.set(true);
    }
}
