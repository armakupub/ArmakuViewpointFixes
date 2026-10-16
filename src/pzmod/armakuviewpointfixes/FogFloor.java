package pzmod.armakuviewpointfixes;

import viewpoint.render.SceneData;

// At the densest fog Viewpoint pulls the fog end in to 40 m at once, shrinking the near world with it.
public class FogFloor {
    public static void reach(SceneData scene, float fog) {
        if (!Settings.FOG_FLOOR.get() || scene.fogEnd >= scene.nearReach) return;
        scene.fogEnd = scene.nearReach;
        scene.fogStart = scene.fogEnd * (0.88f + (0.3f - 0.88f) * fog);
    }
}
