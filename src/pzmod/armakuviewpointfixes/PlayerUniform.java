package pzmod.armakuviewpointfixes;

import java.util.Map;
import viewpoint.input.FreeCam;
import viewpoint.render.FrameContext;
import viewpoint.render.SceneData;
import zombie.characters.IsoPlayer;

// Shader packs' screen-space occlusion darkens the ground around the own character like a cloud.
public class PlayerUniform {
    @SuppressWarnings("unchecked")
    public static void add(Object uniforms, FrameContext frame) {
        if (SkyUniforms.VALUES == null) return;
        try {
            Map<String, float[]> map = (Map<String, float[]>) SkyUniforms.VALUES.get(uniforms);
            IsoPlayer player = IsoPlayer.getInstance();
            if (!Settings.OWN_AO.get() || player == null || player.getVehicle() != null || FreeCam.centre() != null) {
                map.put("vpPlayer", new float[4]);
                return;
            }
            // Viewpoint's scene origin is the character, so the eye and the terrain lift give its feet.
            SceneData scene = frame.scene;
            float lift = scene.relief == null ? 0.0f : scene.relief.scene(-scene.originX, -scene.originZ);
            map.put("vpPlayer", new float[]{frame.eyeX, lift - frame.eyeY, frame.eyeZ, 1.0f});
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
