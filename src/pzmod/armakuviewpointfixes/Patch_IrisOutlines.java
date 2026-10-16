package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

@Patch(className = "viewpoint.render.WorldRenderer", methodName = "drawOutlines")
public class Patch_IrisOutlines {
    @Patch.OnEnter
    public static void enter() {
        IrisOutlines.draw();
    }
}
