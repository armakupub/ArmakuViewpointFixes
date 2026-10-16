package pzmod.armakuviewpointfixes;

import org.joml.Vector3f;
import viewpoint.core.VehicleAhead;
import viewpoint.core.View;
import zombie.characters.IsoPlayer;
import zombie.core.physics.WorldSimulation;
import zombie.vehicles.BaseVehicle;

// Viewpoint keeps VehicleAhead's turn rate private; tracked here by the same rule.
public class VehicleTurn {
    static final int CHAIN = 3;
    static final float MOST_TURN = (float) (2.0 * Math.PI);
    static final Vector3f forward = new Vector3f();
    static final double[] centre = new double[2];

    static final class Rate {
        BaseVehicle vehicle;
        int stepNo;
        float stepYaw;
        float rate;
    }

    static final Rate[] rates = new Rate[CHAIN];

    static {
        for (int i = 0; i < CHAIN; i++) rates[i] = new Rate();
    }

    // Also leaves where Viewpoint draws the vehicle's centre in centre.
    static void track(BaseVehicle vehicle, int link) {
        vehicle.getForwardVector(forward);
        float yaw = (float) Math.atan2(forward.x, forward.z);
        WorldSimulation sim = WorldSimulation.instance;
        int no = sim.getBulletFrameNo();
        Rate r = rates[link];
        if (vehicle != r.vehicle) {
            r.vehicle = vehicle;
            r.rate = 0.0f;
        } else if (no != r.stepNo) {
            float rate = no == r.stepNo + 1 && sim.periodSec > 0.0f
                    ? (float) Math.IEEEremainder(yaw - r.stepYaw, 2.0 * Math.PI) / sim.periodSec : 0.0f;
            r.rate = Math.abs(rate) > MOST_TURN ? 0.0f : rate;
        }
        r.stepNo = no;
        r.stepYaw = yaw;
        centre[0] = vehicle.getX();
        centre[1] = vehicle.getY();
        VehicleAhead.carry(vehicle, centre);
    }

    static BaseVehicle seated() {
        IsoPlayer p = IsoPlayer.players[0];
        return View.enabled && p != null ? p.getVehicle() : null;
    }

    static int link(BaseVehicle vehicle) {
        if (!VehicleAhead.carried(vehicle)) return -1;
        BaseVehicle v = seated();
        for (int i = 0; v != null && i < CHAIN; i++, v = v.getVehicleTowing()) {
            if (v == vehicle) return i;
        }
        return -1;
    }
}
