package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.core.Frame;
import zombie.vehicles.BaseVehicle;

@Patch(className = "viewpoint.light.Headlights", methodName = "car")
public class Patch_HeadlightLead {
    @Patch.OnEnter
    public static void enter(@Patch.Argument(0) Frame frame) {
        HeadlightLead.before(frame);
    }

    @Patch.OnExit
    public static void exit(@Patch.Argument(0) Frame frame, @Patch.Argument(1) BaseVehicle vehicle) {
        HeadlightLead.after(frame, vehicle);
    }
}
