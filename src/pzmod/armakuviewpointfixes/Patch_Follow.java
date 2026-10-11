package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.camera.Follow", methodName = "update")
public class Patch_Follow {
    @Patch.OnEnter
    public static void enter(@Patch.Argument(value = 7, readOnly = false) float dt) {
        dt = FollowClock.step();
    }
}
