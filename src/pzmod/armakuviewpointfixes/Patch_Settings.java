package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.camera.ThirdPerson", methodName = "declare")
public class Patch_Settings {
    @Patch.OnExit
    public static void exit() {
        Settings.declare();
    }
}
