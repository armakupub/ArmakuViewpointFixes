package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.platform.GlProgram;

// Viewpoint tests a pack's 10-25 cm shadow texels by one sprite pixel: wires cast dots, tree crowns noise.
public class ShadowCoverage {
    static final String FILE = "lib/sample_sprite.glsl";
    static final String UNIFORM = "uArmakuShadowCoverage";
    static final String HEAD = "uniform vec2 uAlphaRange = vec2(0.3, 2.0);\n";
    static final String OWN = "float own = textureLod(tex, at, 0.0).a;";
    static final float ALPHA = 0.3f;
    static final Field GL;
    static boolean warned;

    static {
        Field gl = null;
        try {
            gl = Class.forName("viewpoint.render.IrisProgram").getDeclaredField("gl");
            gl.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] sun shadows keep Viewpoint's sprite test, Viewpoint changed: " + e);
        }
        GL = gl;
    }

    public static String patch(String name, String text) {
        if (!FILE.equals(name) || text == null || text.contains(UNIFORM)) return text;
        if (!text.contains(HEAD) || !text.contains(OWN)) {
            if (!warned) System.out.println("[ArmakuViewpointFixes] sprite alpha test not found in " + FILE + ", Viewpoint changed");
            warned = true;
            return text;
        }
        return text.replace(HEAD, HEAD + "uniform int " + UNIFORM + ";\n")
                .replace(OWN, "float own = " + UNIFORM + " != 0 ? c.a : textureLod(tex, at, 0.0).a;");
    }

    public static void uniforms(Object irisProgram, boolean shadow, boolean translucent) {
        if (GL == null) return;
        try {
            GlProgram p = (GlProgram) GL.get(irisProgram);
            if (!p.has(UNIFORM)) return;
            boolean on = shadow && !translucent && Settings.SHADOW_COVERAGE.get();
            p.setInt(UNIFORM, on ? 1 : 0);
            if (on) p.set("uAlphaRange", ALPHA, 2.0f);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
