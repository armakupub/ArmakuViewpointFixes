package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import viewpoint.platform.LiveSettings;

// Under shader packs Viewpoint lights lamps with its hidden "library" pack, not the Default pack's options.
public class LibraryLamps {
    static final String[] OPTIONS = {"lampShadows", "lampShadowResolution"};
    static boolean done;

    public static void show() {
        if (done) return;
        done = true;
        try {
            Class<?> graphics = Class.forName("viewpoint.platform.Graphics");
            Field options = graphics.getDeclaredField("options");
            options.setAccessible(true);
            Method iris = graphics.getDeclaredMethod("irisInUse");
            iris.setAccessible(true);
            Map<?, ?> library = (Map<?, ?>) ((Map<?, ?>) options.get(null)).get("library");
            for (String id : OPTIONS) {
                Object setting = library == null ? null : library.get(id);
                if (setting == null) continue;
                ((LiveSettings.Setting) setting).shownWhen(() -> {
                    try {
                        return iris.invoke(null) != null;
                    } catch (ReflectiveOperationException | RuntimeException e) {
                        return false;
                    }
                });
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] lamp shadow options stay hidden under shader packs, Viewpoint changed: " + e);
        }
    }
}
