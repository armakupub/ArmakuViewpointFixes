package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.util.Map;
import org.lwjgl.opengl.GL20;
import viewpoint.platform.GlProgram;
import viewpoint.render.SceneData;

// Viewpoint hands its puddle levels and rain textures only to its own pack, not to shader packs.
public class Puddles {
    static final String[] NAMES = {"vpPuddles", "vpWetGround", "vpRain", "vpRainClock"};
    static final String TEXTURES = "vpRainTextures";
    // Viewpoint binds its rain textures here every frame.
    static final int TEXTURES_UNIT = 123;
    static final Field VALUES, GL;

    static {
        Field values = null, gl = null;
        try {
            values = Class.forName("viewpoint.render.IrisUniforms").getDeclaredField("values");
            values.setAccessible(true);
            gl = Class.forName("viewpoint.render.IrisProgram").getDeclaredField("gl");
            gl.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] puddles for shader packs off, Viewpoint changed: " + e);
            values = null;
        }
        VALUES = values;
        GL = gl;
    }

    @SuppressWarnings("unchecked")
    public static void add(Object uniforms, SceneData scene) {
        if (VALUES == null) return;
        try {
            Map<String, float[]> map = (Map<String, float[]>) VALUES.get(uniforms);
            // Off sends 0: a program keeps the last value it was given.
            float[] v = Settings.PUDDLES.get()
                    ? new float[]{scene.puddles, scene.wetGround, scene.rain, scene.time % 600.0f}
                    : new float[NAMES.length];
            for (int i = 0; i < NAMES.length; i++) map.put(NAMES[i], new float[]{v[i]});
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }

    // Viewpoint's link check rejects a pack whose sampler has no unit yet.
    public static void link(Object irisProgram) {
        if (GL == null) return;
        try {
            GlProgram p = (GlProgram) GL.get(irisProgram);
            if (!p.has(TEXTURES)) return;
            GL20.glUseProgram(p.id());
            GL20.glUniform1i(p.location(TEXTURES), TEXTURES_UNIT);
            GL20.glUseProgram(0);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }

    public static void textures(Object irisProgram) {
        if (GL == null) return;
        try {
            GlProgram p = (GlProgram) GL.get(irisProgram);
            if (p.has(TEXTURES)) p.setInt(TEXTURES, TEXTURES_UNIT);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
