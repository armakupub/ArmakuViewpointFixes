package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.camera.Follow", methodName = "update")
public class Patch_Trail {
    @Patch.OnExit
    public static void exit(@Patch.This Object self) {
        Trail.followed(self);
    }
}
