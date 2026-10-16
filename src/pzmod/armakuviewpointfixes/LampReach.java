package pzmod.armakuviewpointfixes;

public class LampReach {
    static final float VIEWPOINT = 24.0f;

    public static float scaled(float distance) {
        return distance * VIEWPOINT / Settings.LAMP_REACH.get();
    }
}
