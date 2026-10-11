package pzmod.armakuviewpointfixes;

import java.lang.reflect.Field;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import viewpoint.core.Frame;
import viewpoint.core.View;
import zombie.characters.IsoPlayer;
import zombie.core.physics.WorldSimulation;
import zombie.core.skinnedmodel.model.ModelInstanceRenderData;
import zombie.core.skinnedmodel.model.ModelSlotRenderData;
import zombie.vehicles.BaseVehicle;

// Physics steps every 10 ms and the game draws the last step.
public class VehicleLead {
    static final float STEP = 0.01f;
    static final int CHAIN = 3;
    static final Field LOCAL_TIME;
    static final Vector3f velocity = new Vector3f();
    static final Vector3f forward = new Vector3f();
    static final Vector3f angles = new Vector3f();
    static final Matrix4f turned = new Matrix4f();
    static final Matrix4f seat = new Matrix4f();
    static float x, y;
    // Physics' up axis turns the other way from Look.yaw.
    static float turn;

    static final class Rate {
        BaseVehicle vehicle;
        int stepNo;
        float stepYaw;
        float rate;
    }

    static final Rate[] rates = new Rate[CHAIN];

    static {
        Field f = null;
        try {
            f = WorldSimulation.class.getDeclaredField("localTime");
            f.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            System.out.println("[ArmakuViewpointFixes] vehicles stay on the physics steps, the game changed: " + e);
        }
        LOCAL_TIME = f;
        for (int i = 0; i < CHAIN; i++) rates[i] = new Rate();
    }

    static void lead(BaseVehicle vehicle, int link) {
        x = 0.0f;
        y = 0.0f;
        turn = 0.0f;
        if (LOCAL_TIME == null) return;
        float t;
        try {
            t = Math.max(0.0f, Math.min(STEP, LOCAL_TIME.getFloat(WorldSimulation.instance)));
        } catch (ReflectiveOperationException | RuntimeException e) {
            return;
        }
        vehicle.getLinearVelocity(velocity);
        x = velocity.x * t;
        y = velocity.z * t;
        vehicle.getForwardVector(forward);
        float yaw = (float) Math.atan2(forward.x, forward.z);
        WorldSimulation sim = WorldSimulation.instance;
        int no = sim.getBulletFrameNo();
        Rate r = rates[link];
        if (vehicle != r.vehicle) {
            r.vehicle = vehicle;
            r.rate = 0.0f;
        } else if (no != r.stepNo) {
            r.rate = sim.periodSec > 0.0f ? (float) Math.IEEEremainder(yaw - r.stepYaw, 2.0 * Math.PI) / sim.periodSec : 0.0f;
        }
        r.stepNo = no;
        r.stepYaw = yaw;
        turn = r.rate * t;
    }

    static BaseVehicle seated() {
        IsoPlayer p = IsoPlayer.players[0];
        return View.enabled && p != null ? p.getVehicle() : null;
    }

    public static void pivot(Object character, Object pivot) {
        BaseVehicle vehicle = seated();
        if (vehicle == null || character != IsoPlayer.players[0]) return;
        lead(vehicle, 0);
        double[] p = (double[]) pivot;
        p[0] += x;
        p[1] += y;
    }

    public static float heading(float yaw) {
        BaseVehicle vehicle = seated();
        if (vehicle == null || Float.isNaN(yaw)) return yaw;
        lead(vehicle, 0);
        return yaw - turn;
    }

    // Viewpoint eases the eye, the lead must skip that.
    public static boolean eyeLead(Object character) {
        BaseVehicle vehicle = seated();
        if (vehicle == null || View.thirdPerson || character != IsoPlayer.players[0]) return false;
        lead(vehicle, 0);
        return true;
    }

    public static float leadX() {
        return x;
    }

    public static float leadY() {
        return y;
    }

    public static void eye(Object character, Frame frame) {
        if (!eyeLead(character)) return;
        frame.eyeX -= x;
        frame.eyeZ -= y;
    }

    public static void shift(ModelSlotRenderData data) {
        BaseVehicle vehicle = seated();
        int link = 0;
        while (vehicle != null && data.object != vehicle
                && !(data.inVehicle && data.character != null && data.character.getVehicle() == vehicle)) {
            vehicle = ++link < CHAIN ? vehicle.getVehicleTowing() : null;
        }
        if (vehicle == null) return;
        lead(vehicle, link);
        data.x += x;
        data.y += y;
        if (turn == 0.0f) return;
        // Drawn mirrored in X.
        turned.rotationY(-turn);
        if (data.object == vehicle) {
            for (ModelInstanceRenderData part : data.modelData) turned.mul(part.xfrm, part.xfrm);
            turned.mul(data.vehicleTransform, data.vehicleTransform);
        } else {
            turned.mul(vehicle.vehicleTransform, seat).getEulerAnglesZYX(angles);
            data.vehicleAngleX = (float) Math.toDegrees(angles.x);
            data.vehicleAngleY = (float) Math.toDegrees(angles.y);
            data.vehicleAngleZ = (float) Math.toDegrees(angles.z);
        }
    }
}
