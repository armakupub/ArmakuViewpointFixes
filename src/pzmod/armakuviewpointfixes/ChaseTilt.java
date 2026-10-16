package pzmod.armakuviewpointfixes;

public class ChaseTilt {
    public static float pitch() {
        return (float) Math.toRadians(-Settings.CHASE_TILT.get());
    }
}
