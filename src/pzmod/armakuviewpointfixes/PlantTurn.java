package pzmod.armakuviewpointfixes;

import viewpoint.platform.GlProgram;
import viewpoint.render.SceneData;

// Viewpoint stands every plant's crossed planes at the same angles, so a field of grass shows a grid.
public class PlantTurn {
    static final String FILE = "lib/plant.glsl";
    static final String TURN = "uArmakuPlantTurn", ORIGIN = "uArmakuPlantOrigin";
    static final String DECLARE = "uniform int uPlants;";
    static final String ANGLE = "float angle = -PLANT_PI / 4.0 + float(plane) * PLANT_PI / float(max(planes, 1));";
    static final String ENTRY = "PlantVertex plantVertex(vec3 origin, vec3 eye) {";
    static final double WRAP = 1536.0;
    static boolean warned;

    public static String patch(String name, String text) {
        if (!FILE.equals(name) || text == null || text.contains(TURN)) return text;
        for (String line : new String[]{DECLARE, ANGLE, ENTRY}) {
            int at = text.indexOf(line);
            if (at < 0 || text.indexOf(line, at + 1) >= 0) {
                if (!warned) System.out.println("[ArmakuViewpointFixes] plant planes not found in " + FILE + ", Viewpoint changed");
                warned = true;
                return text;
            }
        }
        String spin = "float armakuPlantSpin(vec3 scene) {\n"
                + "  uvec2 k = uvec2(ivec2(floor(mod(scene.xz + " + ORIGIN + ", " + WRAP + ") * 8.0)));\n"
                + "  uint h = (k.x * 0x27D4EB2Du) ^ (k.y * 0x165667B1u);\n"
                + "  h ^= h >> 15u; h *= 0x85EBCA6Bu; h ^= h >> 13u;\n"
                + "  return float(h & 0xFFFFu) / 65535.0 * PLANT_PI;\n"
                + "}\n\n";
        return text.replace(DECLARE, DECLARE + "\nuniform float " + TURN + ";\nuniform vec2 " + ORIGIN + ";")
                .replace(ENTRY, spin + ENTRY)
                .replace(ANGLE, ANGLE.substring(0, ANGLE.length() - 1) + " + " + TURN + " * armakuPlantSpin(foot + origin);");
    }

    public static void uniforms(SceneData scene, GlProgram p) {
        if (p == null || scene == null || !p.has(TURN)) return;
        p.set(TURN, Settings.PLANT_TURN.get() ? 1f : 0f);
        if (p.has(ORIGIN)) p.set(ORIGIN, (float) (scene.originX - Math.floor(scene.originX / WRAP) * WRAP),
                (float) (scene.originZ - Math.floor(scene.originZ / WRAP) * WRAP));
    }
}
