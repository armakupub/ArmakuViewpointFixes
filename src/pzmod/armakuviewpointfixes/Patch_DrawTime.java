package pzmod.armakuviewpointfixes;

import me.zed_0xff.zombie_buddy.Patch;
import viewpoint.core.Frame;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.model.ModelSlotRenderData;
import zombie.vehicles.BaseVehicle;

public class Patch_DrawTime {
    @Patch(className = "zombie.FPSTracking", methodName = "frameStep")
    public static class Patch_frameStep {
        @Patch.OnEnter
        public static void enter() {
            DrawTime.frameStarted();
        }
    }

    @Patch(className = "viewpoint.models.VehicleCapture", methodName = "capture")
    public static class Patch_VehicleCapture {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(0) Frame frame, @Patch.Argument(1) ModelSlotRenderData data,
                                 @Patch.Argument(2) BaseVehicle vehicle) {
            DrawTime.beforeVehicle(frame, data, vehicle);
        }

        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Frame frame) {
            DrawTime.after(frame);
        }
    }

    @Patch(className = "viewpoint.models.ModelCapture", methodName = "capture")
    public static class Patch_ModelCapture {
        @Patch.OnEnter
        public static void enter(@Patch.Argument(0) Frame frame, @Patch.Argument(1) ModelSlotRenderData data,
                                 @Patch.Argument(2) IsoGameCharacter chr) {
            DrawTime.beforeCharacter(frame, data, chr);
        }

        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Frame frame) {
            DrawTime.after(frame);
        }
    }

    @Patch(className = "viewpoint.SceneDrawer", methodName = "drawFrame")
    public static class Patch_drawFrame {
        @Patch.OnEnter
        public static void enter(@Patch.This Object self) {
            DrawTime.draw(self);
        }
    }

    @Patch(className = "viewpoint.input.Camera", methodName = "eye")
    public static class Patch_eye {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Frame frame, @Patch.Argument(2) Object eye) {
            DrawTime.eye(frame, eye);
        }
    }

    @Patch(className = "viewpoint.camera.CameraView", methodName = "place")
    public static class Patch_place {
        @Patch.OnExit
        public static void exit(@Patch.Argument(0) Frame frame, @Patch.Argument(3) Object eye, @Patch.Return boolean placed) {
            if (placed) DrawTime.eye(frame, eye);
        }
    }
}
