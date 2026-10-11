package pzmod.armakuviewpointfixes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import viewpoint.core.Frame;
import viewpoint.input.Look;
import zombie.GameTime;

// Viewpoint swings the view on the render thread's clock, the vehicle moves by each drawn frame's step.
public class BehindClock {
    static final float MOST = 0.1f;
    static volatile boolean active;
    static Frame capturing;
    static final Map<Frame, Float> steps = new ConcurrentHashMap<>();
    // Render thread.
    static float before, step, rate, leadYaw, pending;
    static long drawnNumber = Long.MIN_VALUE;

    public static void captured(boolean chasing) {
        active = chasing;
        if (capturing != null) steps.put(capturing, chasing ? Math.min(MOST, GameTime.getInstance().getPhysicsSecondsSinceLastUpdate()) : 0.0f);
    }

    public static void capturing(Frame frame) {
        capturing = frame;
        steps.put(frame, 0.0f);
    }

    // Render thread: the swing moves on by the drawn frame's own step, once.
    static void drawing(Frame frame) {
        if (frame.number == drawnNumber) {
            pending = 0.0f;
            return;
        }
        drawnNumber = frame.number;
        Float s = steps.get(frame);
        pending = s == null ? 0.0f : s;
    }

    public static boolean active() {
        return active;
    }

    public static float take() {
        float t = pending;
        pending = 0.0f;
        return t;
    }

    public static void turning(float dt) {
        before = Look.yaw;
        step = dt;
    }

    public static void turned() {
        if (!active) rate = 0.0f;
        else if (step > 0.0f) rate = Look.wrap(Look.yaw - before) / step;
    }

    // The drawn vehicle is moved on by DrawTime, the swing turns on with it.
    public static void lead() {
        if (!active || DrawTime.ahead == 0.0f || rate == 0.0f) return;
        leadYaw = rate * DrawTime.ahead;
        Look.yaw = Look.wrap(Look.yaw + leadYaw);
    }

    public static void unlead() {
        if (leadYaw == 0.0f) return;
        Look.yaw = Look.wrap(Look.yaw - leadYaw);
        leadYaw = 0.0f;
    }
}
