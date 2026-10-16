package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.packs.PackBind;

@Patch(className = "viewpoint.world.Recipe", methodName = "model")
public class Patch_FlatModels {
    @Patch.OnEnter
    public static void enter(@Patch.Argument(1) PackBind bind, @Patch.Argument(6) int square,
                             @Patch.Argument(value = 7, readOnly = false) int flags) {
        flags = FlatModels.flags(bind, square, flags);
    }
}
