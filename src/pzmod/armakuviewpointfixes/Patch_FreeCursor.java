package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_FreeCursor {
    // Viewpoint decides here, every frame, whether it holds the mouse.
    @Patch(className = "viewpoint.FP", methodName = "renderWorld")
    public static class Patch_renderWorld {
        @Patch.OnExit
        public static void exit() {
            WindowCursor.update();
            MenuCursor.update();
        }
    }

    // Render thread, where Viewpoint takes or lets go of the mouse.
    @Patch(className = "viewpoint.input.Look", methodName = "onUpdateMouseCursor")
    public static class Patch_onUpdateMouseCursor {
        @Patch.OnEnter
        public static void enter() {
            MenuCursor.beforeCapture();
        }
    }
}
