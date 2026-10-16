package pzmod.armakuviewpointfixes;

import viewpoint.platform.GlProgram;

// Viewpoint lights roofs as the ceiling of the room below while the camera is under that floor's height.
public class RoofLight {
    static final String FILE = "lib/mesh_vertex.glsl";
    static final String ON = "uArmakuRoofFacing", EYE = "uArmakuEye";
    static final String ORIGIN = "vec2 lightOrigin(MeshVertex v, float eyeY) {";
    static final String CEILING = "bool ceiling = eyeY < v.mesh.y + v.mesh.w && abs(v.normal.y) > 0.5;";
    static boolean warned;

    public static String patch(String name, String text) {
        if (!FILE.equals(name) || text == null || text.contains(ON)) return text;
        if (!text.contains(ORIGIN) || !text.contains(CEILING)) {
            if (!warned) System.out.println("[ArmakuViewpointFixes] roof light: ceiling rule not found in " + FILE + ", Viewpoint changed");
            warned = true;
            return text;
        }
        return text.replace(ORIGIN, "uniform int " + ON + ";\nuniform vec3 " + EYE + ";\n" + ORIGIN)
                .replace(CEILING, "bool ceiling = " + ON + " != 0 && v.normal.y > 0.5 ? dot(v.normal, " + EYE
                        + " - v.pos) < 0.0 : eyeY < v.mesh.y + v.mesh.w && abs(v.normal.y) > 0.5;");
    }

    // uIrisEye is the real eye in every Iris pass; the Iris shadow pass sets uEye to the sun.
    public static void eye(Object program, String name, float x, float y, float z) {
        if (!"uIrisEye".equals(name) && !"uEye".equals(name)) return;
        GlProgram p = (GlProgram) program;
        if (!p.has(ON) || "uEye".equals(name) && p.has("uIrisEye")) return;
        p.setInt(ON, Settings.ROOF_LIGHT.get() ? 1 : 0);
        p.set(EYE, x, y, z);
    }
}
