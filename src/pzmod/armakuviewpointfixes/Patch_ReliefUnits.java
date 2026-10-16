package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_ReliefUnits {
    @Patch(className = "viewpoint.render.IrisProgram", methodName = "checkUnits")
    public static class CheckUnits {
        @Patch.OnEnter
        public static void enter(@Patch.This Object program) {
            ReliefUnits.assign(program);
        }
    }
}
