package pzmod.armakuviewpointfixes;

import imgui.ImGui;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Viewpoint gives subtabs only to its own pages; ours goes through its subtab drawing too.
public class SettingsPage {
    static final String FIXES = "Armaku's Fixes", TWEAKS = "Armaku's Tweaks";
    static final Method NAME, SUBTABS;
    static final Map<String, List<Object>> PAGES;

    static {
        Method name = null, subtabs = null;
        Map<String, List<Object>> pages = null;
        try {
            Class<?> page = Class.forName("viewpoint.platform.SettingsPages$Page");
            Class<?> subtab = Class.forName("viewpoint.platform.SettingsPages$Subtab");
            name = page.getDeclaredMethod("name");
            name.setAccessible(true);
            subtabs = Class.forName("viewpoint.platform.SettingsPages").getDeclaredMethod("subtabs",
                    String.class, List.class, Set.class, Map.class, float.class, Runnable.class);
            subtabs.setAccessible(true);
            Constructor<?> c = subtab.getDeclaredConstructor(String.class, List.class);
            c.setAccessible(true);
            List<Object> fixes = new ArrayList<>(), tweaks = new ArrayList<>();
            for (String leaf : List.of(Settings.CAMERA_LEAF, Settings.DRIVING_LEAF, Settings.LIGHTING_LEAF, Settings.SHADOWS_LEAF,
                    Settings.SKY_LEAF, Settings.WORLD_LEAF, Settings.INTERFACE_LEAF, Settings.ABOUT_LEAF)) {
                fixes.add(c.newInstance(leaf, List.of(leaf)));
            }
            for (String leaf : List.of(Settings.ON_FOOT_LEAF, Settings.DRIVING_LEAF, Settings.INTERFACE_LEAF, Settings.GRAPHICS_LEAF)) {
                tweaks.add(c.newInstance(leaf, List.of(leaf)));
            }
            pages = Map.of(FIXES, fixes, TWEAKS, tweaks);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] settings without subtabs, Viewpoint changed: " + e);
        }
        NAME = name;
        SUBTABS = subtabs;
        PAGES = pages;
    }

    public static boolean draw(Object page, Object tabs, Object places, float width) {
        if (PAGES == null) return false;
        try {
            String tab = (String) NAME.invoke(page);
            List<Object> subtabs = PAGES.get(tab);
            if (subtabs == null) return false;
            Object groups = ((Map<?, ?>) tabs).get(tab);
            SUBTABS.invoke(null, tab, subtabs, groups == null ? Set.of() : groups, places, width, null);
            return true;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return false;
        }
    }

    // Pages under Advanced come in the order their settings were declared; ours always go last.
    public static void last(Object pages) {
        if (NAME == null) return;
        try {
            @SuppressWarnings("unchecked") List<Object> list = (List<Object>) pages;
            for (String tab : List.of(FIXES, TWEAKS)) {
                for (int i = 0; i < list.size(); i++) {
                    if (!tab.equals(NAME.invoke(list.get(i)))) continue;
                    list.add(list.remove(i));
                    break;
                }
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }

    public static void about() {
        ImGui.textWrapped("Armaku's personal fixes and tweaks for Project Viewpoint, made for the way I play Project Zomboid.");
        ImGui.spacing();
        ImGui.textWrapped("Armaku's Fixes: places where Viewpoint goes wrong, sorted by what they touch. Armaku's Tweaks: my own preferences and additions. Each can be switched off.");
        ImGui.spacing();
        ImGui.textWrapped("Always on, without a switch: a shader pack chosen in the main menu loads its relief textures, and the built-in library pack's lamp options show while a shader pack is in use.");
        ImGui.spacing();
        ImGui.textWrapped("Other mods that change the same things, such as the camera, zoom, body turning or vehicle motion, are mostly not compatible. This mod does not step back for them.");
        ImGui.spacing();
        ImGui.textWrapped("On first start the mod sets Viewpoint's camera side offset to 0 and the vehicle camera tilt to 15 degrees, once.");
        ImGui.spacing();
        ImGui.textWrapped("Source: github.com/armakupub/ArmakuViewpointFixes");
    }
}
