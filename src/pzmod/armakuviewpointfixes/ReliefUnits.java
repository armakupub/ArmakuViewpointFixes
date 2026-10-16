package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import org.lwjgl.opengl.GL20;
import viewpoint.platform.GlProgram;

// Viewpoint gives relief samplers their units from the first world frame on; a pack linked in the menu fails.
public class ReliefUnits {
    static final String[] NAMES = {"uRelief", "uReliefFlow", "uReliefWater", "uReliefWalls"};
    static final int[] UNITS = {91, 124, 92, 93};
    static final Field GL;

    static {
        Field gl = null;
        try {
            gl = Class.forName("viewpoint.render.IrisProgram").getDeclaredField("gl");
            gl.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] relief units off, Viewpoint changed: " + e);
        }
        GL = gl;
    }

    public static void assign(Object irisProgram) {
        if (GL == null) return;
        try {
            GlProgram p = (GlProgram) GL.get(irisProgram);
            if (!p.has(NAMES[0])) return;
            GL20.glUseProgram(p.id());
            for (int i = 0; i < NAMES.length; i++) {
                int at = p.location(NAMES[i]);
                if (at >= 0) GL20.glUniform1i(at, UNITS[i]);
            }
            GL20.glUseProgram(0);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
