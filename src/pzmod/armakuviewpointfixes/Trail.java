package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import zombie.characters.IsoPlayer;

// Viewpoint's follow springs trail the body by up to 0.35 m and catch up on stopping, like a zoom in.
public class Trail {
    static final Field X, Y;

    static {
        Field x = null, y = null;
        try {
            Class<?> follow = Class.forName("viewpoint.camera.Follow");
            x = follow.getDeclaredField("x");
            x.setAccessible(true);
            y = follow.getDeclaredField("y");
            y.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] camera trail kept, Viewpoint changed: " + e);
        }
        X = x;
        Y = y;
    }

    public static void followed(Object follow) {
        if (X == null || Stairs.VALUE == null || !Settings.NO_TRAIL.get()) return;
        IsoPlayer p = IsoPlayer.players[0];
        if (p == null || p.getVehicle() != null) return;
        try {
            for (Field f : new Field[]{X, Y}) {
                Object spring = f.get(follow);
                Stairs.VALUE.setFloat(spring, 0.0f);
                Stairs.VELOCITY.setFloat(spring, 0.0f);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
