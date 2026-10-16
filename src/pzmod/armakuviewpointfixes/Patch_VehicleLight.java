package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import zombie.vehicles.BaseVehicle;

public class Patch_VehicleLight {
    @Patch(className = "viewpoint.models.VehicleCapture", methodName = "capture")
    public static class Patch_capture {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(2) BaseVehicle vehicle) {
            VehicleLight.begin(vehicle);
        }

        @Patch.OnExit
        public static void exit() {
            VehicleLight.end();
        }
    }

    @Patch(className = "viewpoint.models.VehicleCapture", methodName = "light")
    public static class Patch_light {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(value = 3, readOnly = false) int light,
                                 @Patch.Argument(value = 4, readOnly = false) float sky) {
            if (!VehicleLight.active) return;
            light = VehicleLight.packed;
            sky = VehicleLight.skyShare;
        }
    }
}
