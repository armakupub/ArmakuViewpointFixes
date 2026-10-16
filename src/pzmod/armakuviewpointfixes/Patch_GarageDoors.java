package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import zombie.iso.sprite.IsoSprite;

@Patch(className = "viewpoint.world.WallMesher", methodName = "wall")
public class Patch_GarageDoors {
    @Patch.OnEnter
    public static void enter(@Patch.Argument(0) IsoSprite sprite) {
        GarageDoors.lift(sprite);
    }
}
