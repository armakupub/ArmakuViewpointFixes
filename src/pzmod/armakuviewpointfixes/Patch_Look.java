package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.input.Look", methodName = "behind")
public class Patch_Look {
    @Patch.OnEnter
    public static void enter(@Patch.Argument(value = 0, readOnly = false) float yaw) {
        yaw = VehicleLead.heading(yaw);
    }
}
