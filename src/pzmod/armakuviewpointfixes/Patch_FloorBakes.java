package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

// Viewpoint dithers a floor with a new bake in over 0.6 s while its built chunk already masks the far world.
public class Patch_FloorBakes {
    @Patch(className = "viewpoint.render.FloorBakes", methodName = "share")
    public static class Share {
        @Patch.OnExit
        public static void exit(@Patch.Return(readOnly = false) float ret) {
            if (Settings.FLOORS_AT_ONCE.get()) ret = 1.0f;
        }
    }
}
