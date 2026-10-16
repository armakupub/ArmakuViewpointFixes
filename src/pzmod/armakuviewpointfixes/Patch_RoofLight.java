package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_RoofLight {
    @Patch(className = "viewpoint.platform.Shaders", methodName = "read")
    public static class Read {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) String name, @Patch.Return(readOnly = false) String text) {
            text = RoofLight.patch(name, text);
        }
    }

    @Patch(className = "viewpoint.platform.GlProgram", methodName = "set")
    public static class Eye {
        @Patch.OnExit
        public static void exit(@Patch.This Object self, @Patch.Argument(0) String name, @Patch.Argument(1) float x,
                                @Patch.Argument(2) float y, @Patch.Argument(3) float z) {
            RoofLight.eye(self, name, x, y, z);
        }
    }
}
