package pzmod.armakuviewpointfixes;

import viewpoint.platform.GlProgram;

// Under shader packs Viewpoint's per-pixel lamp wall check (lampField) flickers on walls while walking.
public class LampFields {
    static final String FILE = "lib/lamp_shadow.glsl";
    static final String UNIFORM = "uArmakuSkipLampFields";
    static final String GUARD = "#define VIEWPOINT_LAMP_SHADOW\n";
    static final String CHECK = "return min(lampField(i, pos, n), lampShadow(i, pos, n));";
    static boolean warned;

    public static String patch(String name, String text) {
        if (!FILE.equals(name) || text == null || text.contains(UNIFORM)) return text;
        if (!text.contains(GUARD) || !text.contains(CHECK)) {
            if (!warned) System.out.println("[ArmakuViewpointFixes] lamp wall check not found in " + FILE + ", Viewpoint changed");
            warned = true;
            return text;
        }
        return text.replace(GUARD, GUARD + "uniform int " + UNIFORM + ";\n")
                .replace(CHECK, "return min(" + UNIFORM + " != 0 ? 1.0 : lampField(i, pos, n), lampShadow(i, pos, n));");
    }

    public static void uniforms(Object program) {
        GlProgram p = (GlProgram) program;
        if (p.has(UNIFORM)) p.setInt(UNIFORM, Settings.SKIP_LAMP_FIELDS.get() ? 1 : 0);
    }
}
