package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_ShadowCoverage {
    @Patch(className = "viewpoint.platform.Shaders", methodName = "read")
    public static class Read {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) String name, @Patch.Return(readOnly = false) String text) {
            text = ShadowCoverage.patch(name, text);
        }
    }

    @Patch(className = "viewpoint.render.IrisGeometry", methodName = "viewpoint")
    public static class Viewpoint {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object program, @Patch.Argument(6) boolean shadow,
                                @Patch.Argument(7) boolean translucent) {
            ShadowCoverage.uniforms(program, shadow, translucent);
        }
    }
}
