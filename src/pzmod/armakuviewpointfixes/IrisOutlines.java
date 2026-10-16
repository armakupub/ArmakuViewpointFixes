package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Viewpoint skips the game's outlines while a shader pack draws the world; the game still composites them.
public class IrisOutlines {
    static final Field IRIS_DRAWN, FRAME, MODELS, RESCALE, TARGETS, HDR_FBO;
    static final Method OUTLINES, OUTLINE_TARGET;

    static {
        Field irisDrawn = null, frame = null, models = null, rescale = null, targets = null, hdrFbo = null;
        Method outlines = null, outlineTarget = null;
        try {
            Class<?> wr = Class.forName("viewpoint.render.WorldRenderer");
            Class<?> ctx = Class.forName("viewpoint.render.FrameContext");
            irisDrawn = open(wr.getDeclaredField("irisDrawn"));
            frame = open(wr.getDeclaredField("frame"));
            models = open(wr.getDeclaredField("models"));
            rescale = open(wr.getDeclaredField("rescale"));
            targets = open(wr.getDeclaredField("targets"));
            hdrFbo = open(Class.forName("viewpoint.render.Targets").getDeclaredField("hdrFbo"));
            outlines = Class.forName("viewpoint.render.ModelPass").getDeclaredMethod("outlines", ctx, int.class);
            outlines.setAccessible(true);
            outlineTarget = Class.forName("viewpoint.render.Rescale").getDeclaredMethod("outlineTarget", ctx, int.class);
            outlineTarget.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            outlines = null;
            System.out.println("[ArmakuViewpointFixes] outlines under shader packs off, Viewpoint changed: " + e);
        }
        IRIS_DRAWN = irisDrawn;
        FRAME = frame;
        MODELS = models;
        RESCALE = rescale;
        TARGETS = targets;
        HDR_FBO = hdrFbo;
        OUTLINES = outlines;
        OUTLINE_TARGET = outlineTarget;
    }

    static Field open(Field f) {
        f.setAccessible(true);
        return f;
    }

    // Render thread.
    public static void draw() {
        if (OUTLINES == null || !Settings.IRIS_OUTLINES.get()) return;
        try {
            if (!IRIS_DRAWN.getBoolean(null)) return;
            Object frame = FRAME.get(null);
            int fbo = (int) OUTLINE_TARGET.invoke(RESCALE.get(null), frame, HDR_FBO.getInt(TARGETS.get(null)));
            OUTLINES.invoke(MODELS.get(null), frame, fbo);
        } catch (ReflectiveOperationException | RuntimeException e) {
        }
    }
}
