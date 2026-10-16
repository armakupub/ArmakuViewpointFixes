package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_Zoom {
    // After Viewpoint's own handler, so the loot menu and settings window get the wheel first.
    @Patch(className = "viewpoint.Hooks", methodName = "mouseUpdated")
    public static class Patch_mouseUpdated {
        @Patch.OnExit
        public static void exit() {
            Zoom.mouseUpdated();
        }
    }

    @Patch(className = "viewpoint.camera.CameraStep", methodName = "settings")
    public static class Patch_settings {
        @Patch.OnExit
        public static void exit(@Patch.This Object self, @Patch.Argument(0) boolean seated) {
            Zoom.distance(self, seated);
        }
    }

    @Patch(className = "viewpoint.camera.Framing", methodName = "right")
    public static class Patch_right {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(value = 1, readOnly = false) float distance) {
            distance = Zoom.framed(distance);
        }
    }

    @Patch(className = "viewpoint.camera.Framing", methodName = "up")
    public static class Patch_up {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(value = 1, readOnly = false) float distance) {
            distance = Zoom.framed(distance);
        }
    }

    @Patch(className = "viewpoint.camera.Framing", methodName = "shifted")
    public static class Patch_shifted {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(value = 2, readOnly = false) float distance) {
            distance = Zoom.framed(distance);
        }
    }
}
