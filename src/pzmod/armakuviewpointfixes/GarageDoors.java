package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import zombie.iso.sprite.IsoSprite;

// walls_garage_01 pieces are 1.6 squares long, coplanar with their neighbours; the overhang carries the art.
public class GarageDoors {
    static final Field LIFT;

    static {
        Field lift = null;
        try {
            lift = Class.forName("viewpoint.world.WorldMesher").getDeclaredField("lift");
            lift.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] garage doors off, Viewpoint changed: " + e);
        }
        LIFT = lift;
    }

    public static void lift(IsoSprite sprite) {
        if (LIFT == null || sprite == null || !Settings.GARAGE_DOORS.get()) return;
        String piece = sprite.getProperties().get("GarageDoor");
        if (piece == null) return;
        try {
            int layer = Math.floorMod(Integer.parseInt(piece.trim()) - 1, 3) + 1;
            LIFT.setInt(null, layer * 16);
        } catch (NumberFormatException | ReflectiveOperationException e) {
        }
    }
}
