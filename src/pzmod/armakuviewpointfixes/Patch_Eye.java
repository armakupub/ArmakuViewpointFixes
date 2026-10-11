package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.core.Frame;

public class Patch_Eye {
    @Patch(className = "viewpoint.input.Eye", methodName = "atHead")
    public static class Patch_atHead {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(0) Object character,
                                 @Patch.Argument(value = 2, readOnly = false) float headX,
                                 @Patch.Argument(value = 4, readOnly = false) float headZ) {
            if (!VehicleLead.eyeLead(character)) return;
            headX += VehicleLead.leadX();
            headZ += VehicleLead.leadY();
        }

        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object character, @Patch.Argument(1) Frame frame) {
            VehicleLead.eye(character, frame);
        }
    }

    @Patch(className = "viewpoint.input.Eye", methodName = "place")
    public static class Patch_place {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object character, @Patch.Argument(1) Frame frame) {
            VehicleLead.eye(character, frame);
        }
    }
}
