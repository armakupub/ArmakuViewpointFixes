package pzmod.armakuviewpointfixes;

import viewpoint.core.CameraSquares;

// Seated, Viewpoint's two-level wall grid starts near the vehicle's floor and caps the camera below the sky.
public class OpenSky {
    public static volatile boolean seated;

    public static boolean open(Object squares, int x, int y, int level) {
        if (!seated || level < CameraSquares.LEVELS || !Settings.OPEN_SKY.get()) return false;
        CameraSquares s = (CameraSquares) squares;
        return x >= 0 && y >= 0 && x < s.side && y < s.side;
    }
}
