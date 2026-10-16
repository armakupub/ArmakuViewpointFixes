package pzmod.armakuviewpointfixes;

import viewpoint.platform.LiveSettings;

public class Settings {
    static final String FIXES = SettingsPage.FIXES, TWEAKS = SettingsPage.TWEAKS;
    static final String CAMERA_LEAF = "Camera", DRIVING_LEAF = "Driving", LIGHTING_LEAF = "Lighting", SHADOWS_LEAF = "Shadows",
            SKY_LEAF = "Sky & weather", WORLD_LEAF = "World", INTERFACE_LEAF = "Interface", ABOUT_LEAF = "About",
            ON_FOOT_LEAF = "On foot", GRAPHICS_LEAF = "Graphics";
    static final String FIX_CAMERA = FIXES + "/" + CAMERA_LEAF;
    static final String FIX_DRIVING = FIXES + "/" + DRIVING_LEAF;
    static final String FIX_LIGHTING = FIXES + "/" + LIGHTING_LEAF;
    static final String FIX_SHADOWS = FIXES + "/" + SHADOWS_LEAF;
    static final String FIX_SKY = FIXES + "/" + SKY_LEAF;
    static final String FIX_WORLD = FIXES + "/" + WORLD_LEAF;
    static final String FIX_INTERFACE = FIXES + "/" + INTERFACE_LEAF;
    static final String ABOUT = FIXES + "/" + ABOUT_LEAF;
    static final String TWEAK_ON_FOOT = TWEAKS + "/" + ON_FOOT_LEAF;
    static final String TWEAK_DRIVING = TWEAKS + "/" + DRIVING_LEAF;
    static final String TWEAK_INTERFACE = TWEAKS + "/" + INTERFACE_LEAF;
    static final String TWEAK_GRAPHICS = TWEAKS + "/" + GRAPHICS_LEAF;

    public static final LiveSettings.Toggle STAIRS_FOLLOW = LiveSettings.toggle("armakuFixes.stairsFollow", "Camera keeps up on stairs", FIX_CAMERA, true);
    public static final LiveSettings.Toggle NO_TRAIL = LiveSettings.toggle("armakuFixes.noTrail", "Camera without trail", FIX_CAMERA, true);
    public static final LiveSettings.Toggle STEADY_CURVES = LiveSettings.toggle("armakuFixes.steadyCurves", "Steady vehicle in curves", FIX_DRIVING, true);
    public static final LiveSettings.Toggle VEHICLE_LIGHT = LiveSettings.toggle("armakuFixes.vehicleLight", "Vehicle light without steps", FIX_DRIVING, true);
    public static final LiveSettings.Toggle OPEN_SKY = LiveSettings.toggle("armakuFixes.openSky", "Open sky above the vehicle", FIX_DRIVING, true);
    public static final LiveSettings.Toggle SKIP_LAMP_FIELDS = LiveSettings.toggle("armakuFixes.skipLampFields", "Lamp light without the wall check", FIX_LIGHTING, true);
    public static final LiveSettings.Toggle ROOF_LIGHT = LiveSettings.toggle("armakuFixes.roofLight", "Roofs without the light of the room below", FIX_LIGHTING, true);
    public static final LiveSettings.Toggle LAMP_SHADOW_FADE = LiveSettings.toggle("armakuFixes.lampShadowFade", "Lamp shadows fade in", FIX_LIGHTING, true);
    public static final LiveSettings.Toggle SHADOW_COVERAGE = LiveSettings.toggle("armakuFixes.shadowCoverage", "Sun shadows from sprite coverage", FIX_SHADOWS, true);
    public static final LiveSettings.Toggle WHOLE_SHADOWS = LiveSettings.toggle("armakuFixes.wholeShadows", "Walls cast sun shadows from every side", FIX_SHADOWS, true);
    public static final LiveSettings.Toggle STEADY_SUN = LiveSettings.toggle("armakuFixes.steadySun", "Sun moves steadily under shader packs", FIX_SKY, true);
    public static final LiveSettings.Toggle FOG_FLOOR = LiveSettings.toggle("armakuFixes.fogFloor", "Dense fog keeps the near world", FIX_SKY, true);
    public static final LiveSettings.Toggle FADE_NOISE = LiveSettings.toggle("armakuFixes.fadeNoise", "Chunks fade in without holes", FIX_WORLD, true);
    public static final LiveSettings.Toggle SHELL_TEXEL = LiveSettings.toggle("armakuFixes.shellTexel", "Far buildings in their own colour", FIX_WORLD, true);
    public static final LiveSettings.Toggle FLOORS_AT_ONCE = LiveSettings.toggle("armakuFixes.floorsAtOnce", "Floors appear at once", FIX_WORLD, true);
    public static final LiveSettings.Toggle FLAT_MODELS = LiveSettings.toggle("armakuFixes.flatModels", "Flat pack models without flicker", FIX_WORLD, true);
    public static final LiveSettings.Toggle GARAGE_DOORS = LiveSettings.toggle("armakuFixes.garageDoors", "Garage doors in front of the wall", FIX_WORLD, true);
    public static final LiveSettings.Toggle CORPSE_REACH = LiveSettings.toggle("armakuFixes.corpseReach", "3D corpses between camera and character", FIX_WORLD, true);
    public static final LiveSettings.Toggle IRIS_OUTLINES = LiveSettings.toggle("armakuFixes.irisOutlines", "Game outlines under shader packs", FIX_INTERFACE, true);
    public static final LiveSettings.Toggle EASED_TURN = LiveSettings.toggle("armakuFixes.easedTurn", "Quick body turn", TWEAK_ON_FOOT, true);
    public static final LiveSettings.Toggle FIRST_START_DONE = LiveSettings.toggle("armakuFixes.firstStartDone", "First start done", TWEAK_ON_FOOT, false);
    public static final LiveSettings.Toggle WHEEL_ZOOM = LiveSettings.toggle("armakuFixes.wheelZoom", "Zoom with the mouse wheel", TWEAK_ON_FOOT, true);
    public static final LiveSettings.Number ZOOM_ON_FOOT = LiveSettings.number("armakuFixes.zoomOnFoot", "Zoom (x)", TWEAK_ON_FOOT, 0.25f, 3.0f, 0.01f, 1.0f);
    public static final LiveSettings.Number ZOOM_NEAR = LiveSettings.number("armakuFixes.zoomNearest", "Zoom: nearest (x)", TWEAK_ON_FOOT, 0.25f, 3.0f, 0.05f, 0.9f);
    public static final LiveSettings.Number ZOOM_FAR = LiveSettings.number("armakuFixes.zoomFarthest", "Zoom: farthest (x)", TWEAK_ON_FOOT, 0.25f, 3.0f, 0.05f, 2.0f);
    public static final LiveSettings.Number ZOOM_STEPS = LiveSettings.number("armakuFixes.zoomSteps", "Zoom: steps", TWEAK_ON_FOOT, 1.0f, 20.0f, 1.0f, 6.0f);
    public static final LiveSettings.Toggle INDOOR_ZOOM = LiveSettings.toggle("armakuFixes.indoorZoom", "Zoom in indoors", TWEAK_ON_FOOT, true);
    public static final LiveSettings.Number INDOOR_ZOOM_FACTOR = LiveSettings.number("armakuFixes.indoorZoomFactor", "Zoom indoors (x)", TWEAK_ON_FOOT, 0.25f, 3.0f, 0.05f, 0.5f);
    public static final LiveSettings.Number CHASE_TILT = LiveSettings.number("armakuFixes.chaseTilt", "Looking down (degrees)", TWEAK_DRIVING, 0.0f, 30.0f, 1.0f, 8.0f);
    public static final LiveSettings.Number CHASE_DELAY = LiveSettings.number("armakuFixes.chaseDelay", "Swing delay (s)", TWEAK_DRIVING, 0.75f, 5.0f, 0.05f, 2.0f);
    public static final LiveSettings.Toggle SEATED_FREE_LOOK = LiveSettings.toggle("armakuFixes.seatedFreeLook", "Free look in the vehicle", TWEAK_DRIVING, true);
    public static final LiveSettings.Number ZOOM_SEATED = LiveSettings.number("armakuFixes.zoomSeated", "Zoom (x)", TWEAK_DRIVING, 0.25f, 3.0f, 0.01f, 1.0f);
    public static final LiveSettings.Number ZOOM_SEATED_NEAR = LiveSettings.number("armakuFixes.zoomSeatedNearest", "Zoom: nearest (x)", TWEAK_DRIVING, 0.25f, 3.0f, 0.05f, 0.7f);
    public static final LiveSettings.Number ZOOM_SEATED_FAR = LiveSettings.number("armakuFixes.zoomSeatedFarthest", "Zoom: farthest (x)", TWEAK_DRIVING, 0.25f, 3.0f, 0.05f, 1.4f);
    public static final LiveSettings.Toggle THIRD_PERSON_START = LiveSettings.toggle("armakuFixes.thirdPersonStart", "Open the 3D view in third person", TWEAK_INTERFACE, true);
    public static final LiveSettings.Toggle FREE_CURSOR = LiveSettings.toggle("armakuFixes.menusFreeCursor", "Free the cursor in menus", TWEAK_INTERFACE, true);
    public static final LiveSettings.Toggle HIDE_LOOT = LiveSettings.toggle("armakuFixes.menusHideLootPanel", "Hide the loot menu behind menus", TWEAK_INTERFACE, true);
    public static final LiveSettings.Toggle INVENTORY_KEY = LiveSettings.toggle("armakuFixes.inventoryKey", "Loot window key opens the inventory", TWEAK_INTERFACE, true);
    public static final LiveSettings.Toggle RAIN = LiveSettings.toggle("armakuFixes.rain", "Rain and snow under shader packs", TWEAK_GRAPHICS, true);
    public static final LiveSettings.Toggle SKY_UNIFORMS = LiveSettings.toggle("armakuFixes.skyUniforms", "Game weather for shader packs", TWEAK_GRAPHICS, true);
    public static final LiveSettings.Toggle PLANT_TURN = LiveSettings.toggle("armakuFixes.plantTurn", "Plants turned at random", TWEAK_GRAPHICS, true);
    public static final LiveSettings.Toggle PUDDLES = LiveSettings.toggle("armakuFixes.puddles", "Game puddles for shader packs", TWEAK_GRAPHICS, true);
    public static final LiveSettings.Toggle OWN_AO = LiveSettings.toggle("armakuFixes.ownAo", "No occlusion cloud around your character", TWEAK_GRAPHICS, true);
    public static final LiveSettings.Toggle VANILLA_SKY = LiveSettings.toggle("armakuFixes.vanillaSky", "Sky darkens with the game's night", TWEAK_GRAPHICS, true);
    public static final LiveSettings.Number LAMP_REACH = LiveSettings.number("armakuFixes.lampReach", "Lamp reach (m)", TWEAK_GRAPHICS, 24.0f, 64.0f, 2.0f, 24.0f);

    static {
        viewpoint.platform.SettingsWindow.panel(ABOUT, SettingsPage::about);
        STEADY_CURVES.describe("The camera swings in behind the vehicle in step with each drawn frame, and the vehicle is drawn where it is when the frame is drawn, so it does not twitch while turning.");
        VEHICLE_LIGHT.describe("Viewpoint lights a whole vehicle with the light of the one square under its centre, so it steps in brightness at every square it crosses near lamps. The light is read between the four nearest squares, as floors and walls are, and eased.");
        CHASE_TILT.describe("How far the camera looks down when it swings in behind the vehicle.");
        CHASE_DELAY.describe("In third person, how long the mouse rests before the camera swings back in behind the moving vehicle. Viewpoint waits 0.75 s.");
        SEATED_FREE_LOOK.describe("Held while seated, Viewpoint's free look key keeps the camera where you look instead of swinging it back in behind the vehicle; letting go swings it back at once.");
        OPEN_SKY.describe("Seated, the camera passes above the two levels Viewpoint checks for walls instead of stopping at an invisible ceiling.");
        STAIRS_FOLLOW.describe("On stairs the camera follows the body's height directly instead of trailing below or above it.");
        NO_TRAIL.describe("On foot, the camera moves with the body instead of trailing it, so it does not rush in when you stop.");
        EASED_TURN.describe("Walking, the body turns to where it goes within Viewpoint's Body turn smoothing, so a wide turn is as quick as a small one, instead of at the game's fixed rate.");
        WHEEL_ZOOM.describe("The mouse wheel moves the camera nearer and farther along the view, on foot and in a vehicle. Shoulder and height stay where Viewpoint's settings put them.");
        ZOOM_ON_FOOT.describe("On foot, times Viewpoint's camera distance. The mouse wheel sets it.");
        ZOOM_NEAR.describe("On foot, the wheel's nearest zoom.");
        ZOOM_FAR.describe("On foot, the wheel's farthest zoom.");
        ZOOM_STEPS.describe("Wheel turns from nearest to farthest, on foot and in a vehicle.");
        INDOOR_ZOOM.describe("On foot, entering a building zooms the camera in, never out, and leaving zooms it back. The mouse wheel indoors moves a zoom of its own.");
        INDOOR_ZOOM_FACTOR.describe("How far in entering a building zooms, times Viewpoint's camera distance.");
        INDOOR_ZOOM_FACTOR.shownWhen(INDOOR_ZOOM::get);
        THIRD_PERSON_START.describe("Switching from the isometric view to Viewpoint's view always lands in third person, on foot and in a vehicle, instead of the view you left it in.");
        FREE_CURSOR.describe("Context menus, radial menus and windows such as the map free the cursor while they are open.");
        HIDE_LOOT.describe("Viewpoint's loot menu steps aside while a context or radial menu is open.");
        INVENTORY_KEY.describe("Without a loot menu to act on, Viewpoint's loot window key opens your inventory with the cursor free; again to close it.");
        ROOF_LIGHT.describe("A roof or floor seen from above takes the light of its own floor. Viewpoint lights it as the ceiling of the room below while the camera is under that floor's height, so roofs glow with the rooms under them.");
        SKIP_LAMP_FIELDS.describe("Lamp light comes from Viewpoint's light grid alone, without its per-pixel wall check. Under Minecraft shader packs that check flickers on walls while walking.");
        LAMP_SHADOW_FADE.describe("Lamp shadows fade in over half a second when a lamp starts to cast them, and fade with distance over the last quarter of the lamp reach as its light does, instead of appearing and vanishing at once.");
        LAMP_REACH.describe("How far lamps light with Viewpoint's per-pixel lamps and cast shadows. Viewpoint uses 24 m; more costs some GPU time with many lamps around.");
        IRIS_OUTLINES.describe("Under Minecraft shader packs, the game's outlines on characters show again, such as the red one on an enemy in reach. Viewpoint leaves them out while a pack draws the world.");
        SHADOW_COVERAGE.describe("Under Minecraft shader packs, sprites cast sun shadow by how much of each shadow map texel they cover, like Viewpoint's own shadows, instead of by one pixel. Thin wires leave the shadow instead of casting dots, tree crowns cast steady patches instead of noise.");
        WHOLE_SHADOWS.describe("Under Minecraft shader packs, walls and floors cast sun shadow whichever way they face the camera, and chunks fading in cast whole shadows instead of dithered ones.");
        VANILLA_SKY.describe("The sun rises and sets with the game's own light, when street lights switch off and on, and sinks below the horizon as the game's night darkens, so the sky matches the game's day in every season.");
        STEADY_SUN.describe("Under Minecraft shader packs Viewpoint's sun holds still around noon in summer and jumps across the sky at noon in winter. It moves evenly from sunrise to sunset instead.");
        RAIN.describe("Under Minecraft shader packs, rain falls in streaks and snow drifts in flakes around you, slanted by the wind, in place of Viewpoint's rain and snow.");
        PLANT_TURN.describe("Viewpoint stands the crossed planes of every grass tuft, bush and small plant at the same angles, so a field shows a pattern. Each plant is turned by its own random angle instead, its shadow with it. Not for grass as one card facing you.");
        SKY_UNIFORMS.describe("Hands the game's overcast, cloud cover, gloom, sun strength, fog range and weather (fog, rain or snow, with their clouds) to shader packs written to read them (vpOvercast, vpCloudCover, vpDesaturation, vpSunStrength, vpFogStart, vpFogEnd, vpWeather). Other packs ignore them.");
        PUDDLES.describe("Hands the game's puddle and wet ground levels and Viewpoint's rain textures to shader packs written to read them (vpPuddles, vpWetGround, vpRain, vpRainClock, vpRainTextures), so they can draw puddles as Viewpoint's own pack does. Other packs ignore them.");
        OWN_AO.describe("Hands where your character stands to shader packs written to read it (vpPlayer), so their screen-space ambient occlusion leaves your body out and the ground around you does not darken like a cloud when the camera is raised. Zombies and others keep it. Other packs ignore it.");
        FOG_FLOOR.describe("At the densest fog Viewpoint pulls the fog end in to 40 m at once and the near world shrinks with it. The fog ends no closer than the near world instead.");
        FADE_NOISE.describe("Under Minecraft shader packs, a chunk fading in is dithered alike in the near and the far world, so no holes show the sky for half a second.");
        CORPSE_REACH.describe("In third person, Viewpoint picks 3D corpses from where the character stands, so corpses between the camera and the character drop out while still on screen, the more the farther the camera is out or in a vehicle. The pick is made from the camera instead.");
        FLAT_MODELS.describe("Model packs build rugs and other flat pieces as cards at one height, each a little larger than its square, so where neighbours overlap they flicker with every camera step. Neighbouring flat pieces are drawn a hair apart in depth instead. Applies to chunks built after the change, so reload the save.");
        GARAGE_DOORS.describe("The game's depth shapes make each piece of a metal garage door 1.6 squares long, so it reaches into the next piece and the wall beside the door and flickers there with every camera step. Each piece is drawn a hair in front of the wall and of its neighbours instead. Applies to chunks built after the change, so reload the save.");
        SHELL_TEXEL.describe("Under Minecraft shader packs Viewpoint hands far buildings and floors their colour twice, as texture and as vertex colour, so packs that multiply both draw them much darker. Packs that read the texture get it once.");
        FLOORS_AT_ONCE.describe("Floors with a new bake appear at once instead of dithering in over half a second with nothing behind them.");
        ZOOM_SEATED.describe("Times Viewpoint's vehicle camera distance. The mouse wheel sets it.");
        ZOOM_SEATED_NEAR.describe("The wheel's nearest zoom in a vehicle.");
        ZOOM_SEATED_FAR.describe("The wheel's farthest zoom in a vehicle.");
        FIRST_START_DONE.shownWhen(() -> false);
        for (LiveSettings.Number n : new LiveSettings.Number[]{ZOOM_ON_FOOT, ZOOM_NEAR, ZOOM_FAR, ZOOM_STEPS, ZOOM_SEATED, ZOOM_SEATED_NEAR, ZOOM_SEATED_FAR}) n.shownWhen(WHEEL_ZOOM::get);
    }

    public static void declare() {
    }
}
