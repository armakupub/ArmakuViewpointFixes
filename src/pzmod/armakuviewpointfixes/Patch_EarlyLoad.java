package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.platform.SettingsWindow", methodName = "update")
public class Patch_EarlyLoad {
    @Patch.OnEnter
    public static void enter() {
        EarlyLoad.check();
    }
}
