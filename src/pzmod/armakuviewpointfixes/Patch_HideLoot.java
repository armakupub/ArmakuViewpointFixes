package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import zombie.characters.IsoPlayer;

@Patch(className = "viewpoint.interact.LootMenu", methodName = "player")
public class Patch_HideLoot {
    @Patch.OnExit
    public static void exit(@Patch.Return(readOnly = false) IsoPlayer ret) {
        if (ret != null && HideLoot.hides()) ret = null;
    }
}
