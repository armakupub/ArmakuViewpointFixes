package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.core.Frame;

public class Patch_Look {
    @Patch(className = "viewpoint.camera.ThirdPerson", methodName = "capture")
    public static class Patch_capture {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(0) Frame frame) {
            BehindClock.capturing(frame);
        }
    }

    @Patch(className = "viewpoint.input.Look", methodName = "behind")
    public static class Patch_behind {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(value = 0, readOnly = false) float yaw) {
            yaw = VehicleLead.heading(yaw);
            BehindClock.captured(!Float.isNaN(yaw));
        }
    }

    @Patch(className = "viewpoint.input.Look", methodName = "turn")
    public static class Patch_turn {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(value = 1, readOnly = false) float dt) {
            if (BehindClock.active()) dt = BehindClock.take();
            BehindClock.turning(dt);
        }

        @Patch.OnExit
        public static void exit() {
            BehindClock.turned();
        }
    }

    @Patch(className = "viewpoint.input.Look", methodName = "readBeforeDrawing")
    public static class Patch_readBeforeDrawing {
        @Patch.OnEnter
        public static void enter() {
            BehindClock.unlead();
        }

        @Patch.OnExit
        public static void exit() {
            BehindClock.lead();
        }
    }
}
