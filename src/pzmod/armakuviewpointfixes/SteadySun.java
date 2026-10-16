package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import viewpoint.render.SceneData;

// Viewpoint places a rotated-path sun by the true sun's height: it holds at noon in summer, flips in winter.
public class SteadySun {
    static final Field SUN, MOON, SUN_ANGLE, SHADOW_ANGLE, WORLD_TIME, SUN_LIGHTS;
    static final Method WORLD_TIME_OF;
    static final double TILT;

    static {
        Field sun = null, moon = null, sunAngle = null, shadowAngle = null, worldTime = null, sunLights = null;
        Method worldTimeOf = null;
        double tilt = 0.0;
        try {
            Class<?> c = Class.forName("viewpoint.render.IrisSun");
            sun = field(c, "sun");
            moon = field(c, "moon");
            sunAngle = field(c, "sunAngle");
            shadowAngle = field(c, "shadowAngle");
            worldTime = field(c, "worldTime");
            sunLights = field(c, "sunLights");
            tilt = field(c, "TILT").getDouble(null);
            worldTimeOf = c.getDeclaredMethod("worldTime", double.class);
            worldTimeOf.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] steady sun off, Viewpoint changed: " + e);
            sun = null;
        }
        SUN = sun;
        MOON = moon;
        SUN_ANGLE = sunAngle;
        SHADOW_ANGLE = shadowAngle;
        WORLD_TIME = worldTime;
        SUN_LIGHTS = sunLights;
        WORLD_TIME_OF = worldTimeOf;
        TILT = tilt;
    }

    static Field field(Class<?> c, String name) throws NoSuchFieldException {
        Field f = c.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    public static void update(Object irisSun, Object scene, boolean rotatedPath) {
        if (!rotatedPath || SUN == null || !Settings.STEADY_SUN.get()) return;
        SceneData s = (SceneData) scene;
        if (s.nightFraction >= 0.0f) return;
        try {
            double d = Math.PI * Math.max(0.0, Math.min(1.0, s.dayFraction));
            double[] sun = (double[]) SUN.get(irisSun), moon = (double[]) MOON.get(irisSun);
            sun[0] = Math.cos(d);
            sun[1] = Math.sin(d) * Math.cos(TILT);
            sun[2] = Math.sin(d) * Math.sin(TILT);
            for (int i = 0; i < 3; i++) moon[i] = -sun[i];
            double angle = d / (Math.PI * 2);
            angle -= Math.floor(angle);
            boolean lights = angle < 0.5;
            SUN_ANGLE.setDouble(irisSun, angle);
            SUN_LIGHTS.setBoolean(irisSun, lights);
            SHADOW_ANGLE.setDouble(irisSun, lights ? angle : angle - 0.5);
            WORLD_TIME.setInt(irisSun, (Integer) WORLD_TIME_OF.invoke(null, angle));
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
