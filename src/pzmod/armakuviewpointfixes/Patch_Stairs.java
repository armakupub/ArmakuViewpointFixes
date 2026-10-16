package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.camera.Follow", methodName = "update")
public class Patch_Stairs {
    @Patch.OnExit
    public static void exit(@Patch.This Object self) {
        Stairs.followed(self);
    }
}
