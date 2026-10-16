package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_FadeNoise {
    @Patch(className = "viewpoint.render.IrisGeometry", methodName = "viewpoint")
    public static class Viewpoint {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object program, @Patch.Argument(1) Object frame,
                                @Patch.Argument(6) boolean shadow) {
            FadeNoise.match(program, frame, shadow);
        }
    }
}
