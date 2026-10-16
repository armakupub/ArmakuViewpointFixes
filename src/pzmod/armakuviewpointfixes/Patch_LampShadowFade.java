package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.core.Frame;

public class Patch_LampShadowFade {
    @Patch(className = "viewpoint.render.LampShadows", methodName = "draw")
    public static class Draw {
        @Patch.OnExit
        public static void exit(@Patch.This Object self) {
            LampShadowFade.drawn(self);
        }
    }

    @Patch(className = "viewpoint.light.Lights", methodName = "put")
    public static class Put {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Frame frame, @Patch.Argument(2) int n) {
            LampShadowFade.put(frame, n);
        }
    }
}
