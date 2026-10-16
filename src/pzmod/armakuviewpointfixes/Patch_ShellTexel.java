package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.iris.IrisAssembler;
import viewpoint.iris.IrisTransform;

public class Patch_ShellTexel {
    @Patch(className = "viewpoint.iris.IrisAssembler", methodName = "fragment")
    public static class Fragment {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) IrisTransform.Stage stage, @Patch.Argument(2) IrisAssembler.Source source,
                                @Patch.Return(readOnly = false) String text) {
            text = ShellTexel.patch(stage, source, text);
        }
    }

    @Patch(className = "viewpoint.render.IrisGeometry", methodName = "begin")
    public static class Begin {
        @Patch.OnExit
        public static void exit(@Patch.Argument(1) Object program) {
            ShellTexel.uniforms(program);
        }
    }
}
