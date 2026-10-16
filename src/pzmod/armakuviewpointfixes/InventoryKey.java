package pzmod.armakuviewpointfixes;

import viewpoint.core.View;
import viewpoint.platform.KeyInput;
import viewpoint.platform.Keys;
import zombie.characters.IsoPlayer;

// Viewpoint leaves its loot window key to the game while its loot panel is not up.
public class InventoryKey {
    static boolean pressed(double key) {
        IsoPlayer p = IsoPlayer.players[0];
        return Settings.INVENTORY_KEY.get() && View.enabled && p != null && !p.isDead() && p.getJoypadBind() == -1
                && KeyInput.menu((int) key) == Keys.LOOT_WINDOW;
    }

    static boolean freeCursor() {
        if (WindowCursor.cursorMode()) return false;
        WindowCursor.setCursorMode(true);
        return true;
    }

    static void holdCursor() {
        WindowCursor.setCursorMode(false);
    }
}
