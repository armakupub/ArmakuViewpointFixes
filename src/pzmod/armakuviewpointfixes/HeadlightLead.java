package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import viewpoint.core.Frame;
import viewpoint.render.SpotDraws;
import zombie.vehicles.BaseVehicle;

// Viewpoint places the vehicle's lamps at capture time; they need DrawTime's gap like the vehicle.
public class HeadlightLead {
    static final int RECORD = 24;
    static final int MAP = 11;
    static final Field SLOTS;
    public static int from;

    static {
        Field f = null;
        try {
            f = Class.forName("viewpoint.light.Headlights").getDeclaredField("slotCar");
            f.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] headlight shadows stay on the physics steps, Viewpoint changed: " + e);
        }
        SLOTS = f;
    }

    public static void before(Frame frame) {
        from = frame.scene.spots.lampCount;
    }

    public static void after(Frame frame, BaseVehicle vehicle) {
        int link = VehicleTurn.link(vehicle);
        if (link < 0) return;
        SpotDraws spots = frame.scene.spots;
        VehicleTurn.track(vehicle, link);
        DrawTime.lamps(frame, vehicle, link, from, spots.lampCount, slot(vehicle, spots),
                frame.camX - (float) VehicleTurn.centre[0], frame.camY - (float) VehicleTurn.centre[1]);
    }

    static int slot(BaseVehicle vehicle, SpotDraws spots) {
        if (SLOTS == null) return -1;
        try {
            int[] cars = (int[]) SLOTS.get(null);
            for (int i = 0; i < cars.length; i++) {
                if (cars[i] == vehicle.vehicleId && spots.carMaps[(i + 1) * MAP] >= 0.0f) return i;
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            return -1;
        }
        return -1;
    }

    // Scene axes: x = camX - x, z = camY - y; turned by a about (px, pz), then shifted by (dx, dz).
    static void move(float[] lamps, float[] maps, int from, int to, int slot, float a, float px, float pz, float dx, float dz) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);
        for (int r = from * RECORD; r < to * RECORD; r += RECORD) {
            point(lamps, r, c, s, px, pz, dx, dz);
            turn(lamps, r + 4, c, s);
            turn(lamps, r + 12, c, s);
            turn(lamps, r + 16, c, s);
        }
        if (slot < 0) return;
        int m = (slot + 1) * MAP;
        point(maps, m + 2, c, s, px, pz, dx, dz);
        turn(maps, m + 5, c, s);
    }

    static void point(float[] v, int i, float c, float s, float px, float pz, float dx, float dz) {
        float x = v[i] - px;
        float z = v[i + 2] - pz;
        v[i] = x * c + z * s + px + dx;
        v[i + 2] = -x * s + z * c + pz + dz;
    }

    static void turn(float[] v, int i, float c, float s) {
        float x = v[i];
        float z = v[i + 2];
        v[i] = x * c + z * s;
        v[i + 2] = -x * s + z * c;
    }
}
