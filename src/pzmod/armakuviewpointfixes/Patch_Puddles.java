package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.render.SceneData;

public class Patch_Puddles {
    @Patch(className = "viewpoint.render.IrisUniforms", methodName = "weather")
    public static class Weather {
        @Patch.OnExit
        public static void exit(@Patch.This Object self, @Patch.Argument(0) SceneData scene) {
            Puddles.add(self, scene);
        }
    }

    @Patch(className = "viewpoint.render.IrisProgram", methodName = "checkUnits")
    public static class CheckUnits {
        @Patch.OnEnter
        public static void enter(@Patch.This Object program) {
            Puddles.link(program);
        }
    }

    @Patch(className = "viewpoint.render.IrisGeometry", methodName = "viewpoint")
    public static class Viewpoint {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Object program) {
            Puddles.textures(program);
        }
    }
}
