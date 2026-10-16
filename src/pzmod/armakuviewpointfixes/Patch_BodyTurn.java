package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.input.Controls", methodName = "walkTowards")
public class Patch_BodyTurn {
    @Patch.OnExit
    public static void exit(@Patch.Argument(0) Object player, @Patch.Argument(1) float angle) {
        BodyTurn.walked(player, angle);
    }
}
