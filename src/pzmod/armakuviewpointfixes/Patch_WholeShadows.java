package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_WholeShadows {
    @Patch(className = "viewpoint.render.IrisLibrary", methodName = "load")
    public static class Load {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) String name, @Patch.Return(readOnly = false) String text) {
            text = WholeShadows.patch(name, text);
        }
    }

    @Patch(className = "viewpoint.render.IrisGeometry", methodName = "viewpoint")
    public static class Viewpoint {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object program) {
            WholeShadows.uniforms(program);
        }
    }
}
