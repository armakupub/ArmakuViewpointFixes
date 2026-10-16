package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.render.FrameContext;

public class Patch_PlayerUniform {
    @Patch(className = "viewpoint.render.IrisUniforms", methodName = "update")
    public static class Update {
        @Patch.OnExit
        public static void exit(@Patch.This Object self, @Patch.Argument(0) FrameContext frame) {
            PlayerUniform.add(self, frame);
        }
    }
}
