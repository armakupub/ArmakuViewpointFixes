package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

// One advice for all three: BehindClock needs the heading ChaseReturn leaves.
@Patch(className = "viewpoint.input.Look", methodName = "behind")
public class Patch_LookBehind {
    @Patch.OnEnter
    public static void enter(@Patch.Argument(value = 0, readOnly = false) float yaw,
                             @Patch.Argument(value = 1, readOnly = false) float pitch) {
        boolean chase = !Float.isNaN(pitch);
        if (chase) pitch = ChaseTilt.pitch();
        yaw = ChaseReturn.heading(yaw, chase);
        BehindClock.captured(!Float.isNaN(yaw));
    }
}
