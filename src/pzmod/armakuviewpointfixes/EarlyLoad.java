package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.platform.LiveSettings;
import viewpoint.platform.SettingsWindow;

// Viewpoint applies the saved settings only when its window loads them first; Mvdox Grass loads earlier.
public class EarlyLoad {
    static boolean checked;

    public static void check() {
        if (checked) return;
        checked = true;
        try {
            Field loaded = LiveSettings.class.getDeclaredField("loaded");
            loaded.setAccessible(true);
            if (!loaded.getBoolean(null)) return;
            SettingsWindow.edited();
            System.out.println("[ArmakuViewpointFixes] settings were loaded before Viewpoint's window; applying them on its first update");
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] early settings load not checked, Viewpoint changed: " + e);
        }
    }
}
