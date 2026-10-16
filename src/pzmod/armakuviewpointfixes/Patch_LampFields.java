package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_LampFields {
    @Patch(className = "viewpoint.platform.Shaders", methodName = "read")
    public static class Read {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) String name, @Patch.Return(readOnly = false) String text) {
            text = LampFields.patch(name, text);
        }
    }

    // Every program shading lamps per pixel, Viewpoint's and a pack's, passes here.
    @Patch(className = "viewpoint.render.SurfacePass", methodName = "lamps")
    public static class Lamps {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object program) {
            LampFields.uniforms(program);
        }
    }
}
