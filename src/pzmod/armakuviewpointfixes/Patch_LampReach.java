package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.light.Lights", methodName = "distance")
public class Patch_LampReach {
    @Patch.OnExit
    public static void exit(@Patch.Return(readOnly = false) float distance) {
        distance = LampReach.scaled(distance);
    }
}
