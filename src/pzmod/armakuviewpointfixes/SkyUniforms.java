package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.util.Map;
import viewpoint.environment.Atmosphere;
import viewpoint.render.SceneData;
import zombie.iso.weather.ClimateManager;

// Viewpoint hands shader packs only rain and snow, not the game's clouds, gloom or unclamped fog range.
public class SkyUniforms {
    static final String[] NAMES = {"vpOvercast", "vpCloudCover", "vpDesaturation", "vpSunStrength", "vpFogStart", "vpFogEnd", "vpWeather"};
    static final Field VALUES;

    static {
        Field values = null;
        try {
            values = Class.forName("viewpoint.render.IrisUniforms").getDeclaredField("values");
            values.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] sky uniforms off, Viewpoint changed: " + e);
        }
        VALUES = values;
    }

    @SuppressWarnings("unchecked")
    public static void add(Object uniforms, SceneData scene) {
        if (VALUES == null) return;
        try {
            Map<String, float[]> map = (Map<String, float[]>) VALUES.get(uniforms);
            // Off sends 0: a program keeps the last value it was given.
            float[] v = Settings.SKY_UNIFORMS.get()
                    ? new float[]{scene.overcast, scene.cloudCover, scene.desaturation, scene.sunStrength, scene.fogStart, scene.fogEnd, weather(scene)}
                    : new float[NAMES.length];
            for (int i = 0; i < NAMES.length; i++) map.put(NAMES[i], new float[]{v[i]});
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }

    // Viewpoint's overcast reaches 0.8 from clouds alone, a veil on sunny days.
    static float weather(SceneData scene) {
        ClimateManager climate = ClimateManager.getInstance();
        float fog = climate == null ? 0f : Math.max(0f, Math.min(1f, climate.getFogIntensity()));
        float event = Math.max(fog, Math.max(scene.rain, scene.snow));
        float gate = Math.min(1f, event / 0.1f);
        return Math.max(event, Atmosphere.overcast(scene.cloudCover, 0f, 0f) * gate * gate * (3f - 2f * gate));
    }
}
