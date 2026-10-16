package pzmod.armakuviewpointfixes;

import viewpoint.packs.PackBind;

// Pack rugs overlap their neighbours coplanar; Viewpoint's layer bits (pulled() in mesh_vertex.glsl) part them.
public class FlatModels {
    static final float THIN = 0.06f;
    static final int LAYER = 16, LAYER_MASK = 3 * LAYER;

    public static int flags(PackBind bind, int square, int flags) {
        if (!Settings.FLAT_MODELS.get() || bind == null || bind.model == null || (flags & LAYER_MASK) != 0) return flags;
        float[] b = bind.model.bounds();
        if (b == null) return flags;
        float thin = Math.min(b[3] - b[0], Math.min(b[4] - b[1], b[5] - b[2])) * bind.scale;
        if (thin > THIN) return flags;
        int x = square & 7, y = (square >> 3) & 7;
        return flags | ((x & 1) + 2 * (y & 1)) * LAYER;
    }
}
