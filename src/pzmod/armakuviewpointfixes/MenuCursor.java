package pzmod.armakuviewpointfixes;

import java.util.ArrayList;
import viewpoint.input.Look;
import zombie.characters.IsoPlayer;
import zombie.ui.UIElementInterface;
import zombie.ui.UIManager;

// Viewpoint holds the mouse through context and radial menus.
public class MenuCursor {
    // The render thread can grab the mouse between Viewpoint's request and update(), recentring the cursor.
    static volatile boolean held;

    public static void update() {
        held = Settings.FREE_CURSOR.get() && menuUp();
        if (Look.wantCapture && held) Look.wantCapture = false;
    }

    // Render thread.
    public static void beforeCapture() {
        if (held) Look.wantCapture = false;
    }

    static boolean menuUp() {
        IsoPlayer p = IsoPlayer.players[0];
        return p != null && p.getJoypadBind() == -1 && menuShown();
    }

    // Not UIManager.isForceCursorVisible, which also counts whatever is under the pinned cursor.
    static boolean menuShown() {
        ArrayList<UIElementInterface> ui = UIManager.getUI();
        for (int i = ui.size() - 1; i >= 0; i--) {
            UIElementInterface element = ui.get(i);
            if (element.isForceCursorVisible() && Boolean.TRUE.equals(element.isVisible())) return true;
        }
        return false;
    }
}
