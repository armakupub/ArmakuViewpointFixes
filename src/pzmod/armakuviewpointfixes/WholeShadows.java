package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.platform.GlProgram;

// Viewpoint's camera checks run in the pack's shadow pass: back-facing walls cast nothing, fading chunks dither.
public class WholeShadows {
    static final String FILE = "iris/mesh_fragment.glsl";
    static final String UNIFORM = "uArmakuWholeShadows";
    static final String SKIP = "(" + UNIFORM + " == 0 || uIrisShadow == 0)";
    static final String[][] EDITS = {
        {"uniform int uIrisTranslucent;", "uniform int uIrisTranslucent;\nuniform int uIrisShadow;\nuniform int " + UNIFORM + ";"},
        {"vp_fadeDither();", "if " + SKIP + " vp_fadeDither();"},
        {"if ((vp_vFlags & 64) != 0 && vp_fadeNoise() >= vp_floorShare(vp_vFloorLayer)) discard;",
         "if (" + SKIP + " && (vp_vFlags & 64) != 0 && vp_fadeNoise() >= vp_floorShare(vp_vFloorLayer)) discard;"},
        {"if (!vp_foliage() && (vp_vFlags & 1) != 0 && dot(n, vp_vPos - uIrisEye) > 0.0) discard;",
         "if (" + SKIP + " && !vp_foliage() && (vp_vFlags & 1) != 0 && dot(n, vp_vPos - uIrisEye) > 0.0) discard;"},
    };
    static final Field GL;
    static boolean warned;

    static {
        Field gl = null;
        try {
            gl = Class.forName("viewpoint.render.IrisProgram").getDeclaredField("gl");
            gl.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] whole sun shadows off, Viewpoint changed: " + e);
        }
        GL = gl;
    }

    public static String patch(String name, String text) {
        if (!FILE.equals(name) || text == null || text.contains(UNIFORM)) return text;
        for (String[] edit : EDITS) {
            int at = text.indexOf(edit[0]);
            if (at < 0 || text.indexOf(edit[0], at + 1) >= 0) {
                if (!warned) System.out.println("[ArmakuViewpointFixes] shadow pass checks not found in " + FILE + ", Viewpoint changed");
                warned = true;
                return text;
            }
        }
        for (String[] edit : EDITS) text = text.replace(edit[0], edit[1]);
        return text;
    }

    public static void uniforms(Object irisProgram) {
        if (GL == null) return;
        try {
            GlProgram p = (GlProgram) GL.get(irisProgram);
            if (p.has(UNIFORM)) p.setInt(UNIFORM, Settings.WHOLE_SHADOWS.get() ? 1 : 0);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
