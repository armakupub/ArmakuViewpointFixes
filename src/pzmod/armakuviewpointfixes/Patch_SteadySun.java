package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_SteadySun {
    @Patch(className = "viewpoint.render.IrisSun", methodName = "update")
    public static class Update {
        @Patch.OnExit
        public static void exit(@Patch.This Object sun, @Patch.Argument(0) Object scene, @Patch.Argument(1) boolean rotatedPath) {
            SteadySun.update(sun, scene, rotatedPath);
        }
    }
}
