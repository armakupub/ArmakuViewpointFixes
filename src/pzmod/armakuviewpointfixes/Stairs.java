package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;

// Viewpoint's height spring trails a steady climb by about 0.22 m per m/s, up to its 0.5 m clamp.
public class Stairs {
    static final Field H, VALUE, VELOCITY;

    static {
        Field h = null, value = null, velocity = null;
        try {
            h = Class.forName("viewpoint.camera.Follow").getDeclaredField("h");
            h.setAccessible(true);
            Class<?> spring = Class.forName("viewpoint.camera.Spring");
            value = spring.getDeclaredField("value");
            value.setAccessible(true);
            velocity = spring.getDeclaredField("velocity");
            velocity.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] camera height eased on stairs, Viewpoint changed: " + e);
        }
        H = h;
        VALUE = value;
        VELOCITY = velocity;
    }

    public static void followed(Object follow) {
        if (H == null || !Settings.STAIRS_FOLLOW.get()) return;
        IsoPlayer p = IsoPlayer.players[0];
        if (p == null || p.getVehicle() != null) return;
        IsoGridSquare sq = p.getCurrentSquare();
        if (sq == null || !sq.HasStairs()) return;
        try {
            Object h = H.get(follow);
            VALUE.setFloat(h, 0.0f);
            VELOCITY.setFloat(h, 0.0f);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
