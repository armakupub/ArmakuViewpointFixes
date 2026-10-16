package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.render.SceneData;

@Patch(className = "viewpoint.environment.Atmosphere", methodName = "fogReach")
public class Patch_FogFloor {
    @Patch.OnExit
    public static void exit(@Patch.Argument(0) SceneData scene, @Patch.Argument(1) float fog) {
        FogFloor.reach(scene, fog);
    }
}
