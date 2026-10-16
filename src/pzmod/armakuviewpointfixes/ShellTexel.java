package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import viewpoint.iris.IrisAssembler;
import viewpoint.iris.IrisTransform;
import viewpoint.platform.GlProgram;

// Viewpoint hands far shells their texel twice, as albedo and in the vertex colour, so packs square it.
public class ShellTexel {
    static final String UNIFORM = "uArmakuShellTexelOnce";
    static final Pattern CARRY = Pattern.compile("(?m)^(\\s*)(\\w+\\.rgb = vp_carriedColour\\(\\w+\\.rgb\\);)$");
    static final String MAIN = "\nvoid main() {\n";
    static final Field GL;
    static boolean warned;

    static {
        Field gl = null;
        try {
            gl = Class.forName("viewpoint.render.IrisProgram").getDeclaredField("gl");
            gl.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] far shell texel fix off, Viewpoint changed: " + e);
        }
        GL = gl;
    }

    public static String patch(IrisTransform.Stage stage, IrisAssembler.Source source, String text) {
        // Viewpoint counts the albedo reads but never uses the count.
        if (source != IrisAssembler.Source.FAR_SHELL || stage == null || stage.albedoReads == 0 || text == null || text.contains(UNIFORM)) return text;
        Matcher m = CARRY.matcher(text);
        int main = text.lastIndexOf(MAIN);
        if (!m.find() || m.find() || main < 0) {
            if (!warned) System.out.println("[ArmakuViewpointFixes] far shell colour carry not found, Viewpoint changed");
            warned = true;
            return text;
        }
        text = CARRY.matcher(text).replaceFirst("$1if (" + UNIFORM + " == 0) $2");
        main = text.lastIndexOf(MAIN);
        return text.substring(0, main) + "\nuniform int " + UNIFORM + ";" + text.substring(main);
    }

    public static void uniforms(Object irisProgram) {
        if (GL == null) return;
        try {
            GlProgram p = (GlProgram) GL.get(irisProgram);
            if (p.has(UNIFORM)) p.setInt(UNIFORM, Settings.SHELL_TEXEL.get() ? 1 : 0);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
