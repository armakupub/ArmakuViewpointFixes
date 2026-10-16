package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.core.Frame;

// Viewpoint shows a new lamp shadow within 30 ms and drops it hard at 24 m, where the light eases out.
public class LampShadowFade {
    static final float SECONDS = 0.5f;
    static final float MOST_STEP = 0.1f;
    static final float EASE_FROM = 18.0f;
    static final Field KEYS, SHOWN, DISTANCE, ORDER;
    static long[] keys = new long[0];
    static float[] fade = new float[0];
    static long last;

    static {
        Field k = null, s = null;
        try {
            Class<?> c = Class.forName("viewpoint.render.LampShadows");
            k = c.getDeclaredField("keys");
            k.setAccessible(true);
            s = c.getDeclaredField("shown");
            s.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] lamp shadows appear at once, Viewpoint changed: " + e);
        }
        KEYS = k;
        SHOWN = s;
        Field d = null, o = null;
        try {
            Class<?> c = Class.forName("viewpoint.light.Lights");
            d = c.getDeclaredField("distance");
            d.setAccessible(true);
            o = c.getDeclaredField("order");
            o.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] lamp shadows end hard at 24 m, Viewpoint changed: " + e);
        }
        DISTANCE = d;
        ORDER = o;
    }

    public static void drawn(Object lampShadows) {
        long now = System.nanoTime();
        float dt = last == 0 ? 0.0f : Math.min(MOST_STEP, (now - last) / 1.0e9f);
        last = now;
        if (KEYS == null || !Settings.LAMP_SHADOW_FADE.get()) return;
        try {
            long[] k = (long[]) KEYS.get(lampShadows);
            float[] shown = (float[]) SHOWN.get(lampShadows);
            if (keys.length != k.length) {
                keys = new long[k.length];
                fade = new float[k.length];
            }
            for (int i = 0; i < k.length && i < shown.length; i++) {
                if (keys[i] != k[i]) {
                    keys[i] = k[i];
                    fade[i] = 0.0f;
                }
                fade[i] = Math.min(shown[i], fade[i] + dt / SECONDS);
                shown[i] = fade[i];
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }

    public static void put(Frame frame, int n) {
        if (DISTANCE == null || !Settings.LAMP_SHADOW_FADE.get()) return;
        try {
            float d = ((float[]) DISTANCE.get(null))[((Integer[]) ORDER.get(null))[n]];
            int i = (frame.scene.lightCount - 1) * 12 + 9;
            if (i >= 9) frame.scene.lights[i] *= smoothstep(LampReach.VIEWPOINT, EASE_FROM, d);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }

    static float smoothstep(float from, float to, float x) {
        float t = Math.max(0.0f, Math.min(1.0f, (x - from) / (to - from)));
        return t * t * (3.0f - 2.0f * t);
    }
}
