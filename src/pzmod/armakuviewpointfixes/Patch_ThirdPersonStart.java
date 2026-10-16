package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_ThirdPersonStart {
    @Patch(className = "viewpoint.FP", methodName = "switchView")
    public static class SwitchView {
        @Patch.OnExit
        public static void exit(@Patch.Argument(1) boolean on) {
            ThirdPersonStart.switched(on);
        }
    }
}
