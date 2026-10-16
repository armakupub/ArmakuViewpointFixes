package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.platform.SettingsWindow", methodName = "update")
public class Patch_LibraryLamps {
    @Patch.OnExit
    public static void exit() {
        LibraryLamps.show();
    }
}
