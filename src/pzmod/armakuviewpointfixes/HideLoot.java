package pzmod.armakuviewpointfixes;

// Viewpoint's loot panel sits over radial and context menus.
public class HideLoot {
    // Viewpoint shows its loot panel only while it holds the mouse, and builds it before update().
    public static boolean hides() {
        return Settings.HIDE_LOOT.get() && MenuCursor.menuUp();
    }
}
