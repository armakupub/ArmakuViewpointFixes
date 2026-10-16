package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import viewpoint.platform.GlProgram;

// Viewpoint dithers a fading chunk with noise 0 near and the frame's noise in IrisFar, so the sky shows.
public class FadeNoise {
    static final Field GL;
    static final Method NOISE;

    static {
        Field gl = null;
        Method noise = null;
        try {
            gl = Class.forName("viewpoint.render.IrisProgram").getDeclaredField("gl");
            gl.setAccessible(true);
            noise = Class.forName("viewpoint.render.FrameContext").getDeclaredMethod("fadeNoise");
            noise.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] fade noise off, Viewpoint changed: " + e);
            gl = null;
        }
        GL = gl;
        NOISE = noise;
    }

    public static void match(Object irisProgram, Object frame, boolean shadow) {
        if (GL == null || shadow || !Settings.FADE_NOISE.get()) return;
        try {
            ((GlProgram) GL.get(irisProgram)).set("uFadeNoise", (Float) NOISE.invoke(frame));
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
