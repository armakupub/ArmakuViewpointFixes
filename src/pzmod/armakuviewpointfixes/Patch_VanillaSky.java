package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;

public class Patch_VanillaSky {
    @Patch(className = "viewpoint.environment.SkyBodies", methodName = "place")
    public static class Place {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(1) double lat, @Patch.Argument(2) double days,
                                 @Patch.Argument(value = 3, readOnly = false) double hourAngle) {
            hourAngle = VanillaSky.hourAngle(lat, days, hourAngle);
        }
    }
}
