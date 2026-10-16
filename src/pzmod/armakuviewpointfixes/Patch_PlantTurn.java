package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.platform.GlProgram;
import viewpoint.render.SceneData;

public class Patch_PlantTurn {
    @Patch(className = "viewpoint.platform.Shaders", methodName = "read")
    public static class Read {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) String name, @Patch.Return(readOnly = false) String text) {
            text = PlantTurn.patch(name, text);
        }
    }

    // Every near mesh draw, native, Iris and their shadow passes, goes through here.
    @Patch(className = "viewpoint.render.Meshes", methodName = "draw")
    public static class Draw {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(0) SceneData scene, @Patch.Argument(1) GlProgram program) {
            PlantTurn.uniforms(scene, program);
        }
    }
}
