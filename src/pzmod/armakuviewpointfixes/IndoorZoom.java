package pzmod.armakuviewpointfixes;

import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;

public class IndoorZoom {
    static final float SETTLE_IN = 0.15f;
    static final float SETTLE_OUT = 0.25f;
    static final long GRACE_NANOS = 3_000_000_000L;
    static boolean inside, left;
    static float unsettled;
    static long leftAt;
    static float zoom = -1.0f;

    static void update(IsoPlayer p, boolean seated, float dt, long now) {
        if (seated || p == null || !Settings.INDOOR_ZOOM.get()) {
            if (inside) leftAt = now;
            inside = false;
            unsettled = 0.0f;
            return;
        }
        IsoGridSquare sq = p.getCurrentSquare();
        boolean under = sq != null && (sq.isInARoom() || !sq.isOutside());
        unsettled = under == inside ? 0.0f : unsettled + dt;
        if (unsettled < (under ? SETTLE_IN : SETTLE_OUT)) return;
        unsettled = 0.0f;
        inside = under;
        if (!inside) {
            leftAt = now;
            left = true;
        } else if (zoom < 0.0f || now - leftAt > GRACE_NANOS) {
            zoom = Math.min(Zoom.outdoors(), Settings.INDOOR_ZOOM_FACTOR.get());
        }
    }

    static boolean active() {
        return inside && zoom >= 0.0f;
    }
}
