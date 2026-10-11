package pzmod.armakuviewpointfixes;

import zombie.GameTime;

// Root motion moves the body by the previous frame's delta.
public class FollowClock {
    static final float MOST = 0.1f;
    static float ahead = Float.NaN;

    public static float step() {
        float step = ahead;
        ahead = GameTime.getInstance().getTimeDelta();
        return Float.isNaN(step) ? 0.0f : Math.max(0.0f, Math.min(MOST, step));
    }
}
