package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.camera.Director", methodName = "pivot")
public class Patch_Director {
    @Patch.OnExit
    public static void exit(@Patch.Argument(1) Object character, @Patch.Argument(3) Object pivot) {
        VehicleLead.pivot(character, pivot);
    }
}
