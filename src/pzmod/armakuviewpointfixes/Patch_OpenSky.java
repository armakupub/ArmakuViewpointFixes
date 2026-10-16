package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_OpenSky {
    @Patch(className = "viewpoint.camera.CameraStep", methodName = "settings")
    public static class Patch_settings {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) boolean seated) {
            OpenSky.seated = seated;
        }
    }

    @Patch(className = "viewpoint.core.CameraSquares", methodName = "at")
    public static class Patch_at {
        @Patch.OnExit
        public static void exit(@Patch.This Object self, @Patch.Argument(0) int x, @Patch.Argument(1) int y,
                                @Patch.Argument(2) int level, @Patch.Return(readOnly = false) int at) {
            if (at < 0 && OpenSky.open(self, x, y, level)) at = 0;
        }
    }
}
