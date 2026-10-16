package pzmod.armakuviewpointfixes;

import java.lang.reflect.Method;
import viewpoint.core.View;
import zombie.characters.IsoPlayer;

// In third person Viewpoint leaves the body turn to the game's fixed rate instead of its own eased turn.
public class BodyTurn {
    static final Method TURN;

    static {
        Method m = null;
        try {
            m = Class.forName("viewpoint.input.Controls").getDeclaredMethod("turn", IsoPlayer.class, float.class, boolean.class);
            m.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] body turns at the game's rate, Viewpoint changed: " + e);
        }
        TURN = m;
    }

    public static void walked(Object player, float angle) {
        if (TURN == null || !View.thirdPerson || !Settings.EASED_TURN.get()) return;
        try {
            TURN.invoke(null, player, angle, false);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] body turns at the game's rate: " + e);
        }
    }
}
