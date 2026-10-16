package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.render.SceneData;

public class Patch_SkyUniforms {
    @Patch(className = "viewpoint.render.IrisUniforms", methodName = "weather")
    public static class Weather {
        @Patch.OnExit
        public static void exit(@Patch.This Object self, @Patch.Argument(0) SceneData scene) {
            SkyUniforms.add(self, scene);
        }
    }
}
