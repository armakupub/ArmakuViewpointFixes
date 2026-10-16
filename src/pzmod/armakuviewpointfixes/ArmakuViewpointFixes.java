package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Exposer;

@Exposer.LuaClass
public class ArmakuViewpointFixes {
    public static boolean inventoryKey(double key) {
        return InventoryKey.pressed(key);
    }

    public static boolean freeCursor() {
        return InventoryKey.freeCursor();
    }

    public static void holdCursor() {
        InventoryKey.holdCursor();
    }
}
