package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.core.Frame;

@Patch(className = "viewpoint.models.CorpseView", methodName = "begin")
public class Patch_CorpseReach {
    @Patch.OnExit
    public static void exit(@Patch.Argument(0) Frame frame) {
        CorpseReach.begin(frame);
    }
}
