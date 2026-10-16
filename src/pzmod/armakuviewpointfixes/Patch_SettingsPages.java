package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_SettingsPages {
    @Patch(className = "viewpoint.platform.SettingsPages", methodName = "body")
    public static class Patch_body {
        @Patch.OnEnter(skipOn = true)
        public static boolean enter(@Patch.Argument(0) Object page, @Patch.Argument(1) Object tabs,
                                    @Patch.Argument(2) Object places, @Patch.Argument(3) float width) {
            return SettingsPage.draw(page, tabs, places, width);
        }
    }

    @Patch(className = "viewpoint.platform.SettingsPages", methodName = "more")
    public static class Patch_more {
        @Patch.OnExit
        public static void exit(@Patch.Return Object pages) {
            SettingsPage.last(pages);
        }
    }
}
