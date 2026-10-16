package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.render.SceneData;

public class Patch_Rain {
    @Patch(className = "viewpoint.render.IrisWeather", methodName = "update")
    public static class Update {
        @Patch.OnEnter(skipOn = true)
        public static boolean enter() {
            return Rain.active();
        }

        @Patch.OnExit
        public static void exit(@Patch.Argument(0) SceneData scene, @Patch.Argument(1) float eyeX,
                                @Patch.Argument(2) float eyeZ, @Patch.Argument(3) double time,
                                @Patch.Return(readOnly = false) boolean ret) {
            if (Rain.active()) ret = Rain.update(scene, eyeX, eyeZ, time);
        }
    }

    @Patch(className = "viewpoint.render.IrisWeather", methodName = "draw")
    public static class Draw {
        @Patch.OnEnter(skipOn = true)
        public static boolean enter() {
            if (!Rain.active()) return false;
            Rain.draw();
            return true;
        }
    }

    @Patch(className = "viewpoint.render.IrisWeather", methodName = "release")
    public static class Release {
        @Patch.OnExit
        public static void exit() {
            Rain.release();
        }
    }
}
