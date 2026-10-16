package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.platform.LiveSettings;
import viewpoint.platform.SettingsWindow;

// Once Viewpoint has read its file; a fresh file gets the values again.
public class FirstStart {
    static boolean checked;

    public static void check() {
        if (checked) return;
        try {
            Field loaded = LiveSettings.class.getDeclaredField("loaded");
            loaded.setAccessible(true);
            if (!loaded.getBoolean(null)) return;
            checked = true;
            if (Settings.FIRST_START_DONE.get()) return;
            Field side = Class.forName("viewpoint.camera.CameraSettings").getDeclaredField("SIDE");
            side.setAccessible(true);
            ((LiveSettings.Number) side.get(null)).set(0.0f);
            Settings.CHASE_TILT.set(15.0f);
            Settings.FIRST_START_DONE.set(true);
            SettingsWindow.edited();
            System.out.println("[ArmakuViewpointFixes] first start: camera side offset 0, looking down 15");
        } catch (ReflectiveOperationException | RuntimeException e) {
            checked = true;
            System.out.println("[ArmakuViewpointFixes] first start values not set, Viewpoint changed: " + e);
        }
    }
}
