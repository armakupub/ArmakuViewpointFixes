# Armaku's Fixes for Project Viewpoint

My personal fixes and tweaks for [Project Viewpoint](https://steamcommunity.com/sharedfiles/filedetails/?id=3809306528) in Project Zomboid B42.

This is a personal collection, made for my own game:

- It works with the Viewpoint version current at its release. I do not keep it working across Viewpoint updates.
- It is not compatible with other mods or fixes from the Workshop that change the same things, and I do not add compatibility unless I need it myself.
- Some settings work together with shader packs or mods of my own that are not public. Without them these settings may be of no use to you.
- Each fix and tweak can be switched off on the pages "Armaku's Fixes" and "Armaku's Tweaks" of Viewpoint's settings. If something breaks or clashes, switch off the fix in question or the whole mod.
- This is a Java mod: through ZombieBuddy it changes the bytecode of the game and of Viewpoint while they run, with the same rights as the game itself. Check the code yourself before you build or install it.

If you find a problem in the collection that I have missed, please report it under [Issues](https://github.com/armakupub/ArmakuViewpointFixes/issues). I may not be playing Project Zomboid or Viewpoint at the time, so it can take a while until I update anything.

## Armaku's Fixes

Places where Viewpoint goes wrong.

### Camera

- **Camera keeps up on stairs**: on stairs the camera follows the body's height directly instead of trailing below or above it.
- **Camera without trail**: on foot the camera moves with the body instead of trailing it, so it does not rush in when you stop.

### Driving

- **Steady vehicle in curves**: the camera swings in behind the vehicle in step with each drawn frame, and the vehicle is drawn where it is when the frame is drawn, so it does not twitch while turning.
- **Vehicle light without steps**: a vehicle takes its light from the four nearest squares, eased, instead of from the one square under its centre, so it no longer steps in brightness near lamps.
- **Open sky above the vehicle**: seated, the camera passes above the two levels Viewpoint checks for walls instead of stopping at an invisible ceiling.

### Lighting

- **Lamp light without the wall check**: lamp light comes from Viewpoint's light grid alone, without its per-pixel wall check, which flickers on walls while walking under Minecraft shader packs.
- **Roofs without the light of the room below**: a roof or floor seen from above takes the light of its own floor instead of glowing with the room under it.
- **Lamp shadows fade in**: lamp shadows fade in over half a second and fade out over the last quarter of the lamp reach, as the light does, instead of appearing and vanishing at once.

### Shadows

- **Sun shadows from sprite coverage**: under Minecraft shader packs, sprites cast sun shadow by how much of each shadow map texel they cover, so thin wires cast no dots and tree crowns cast steady patches instead of noise.
- **Walls cast sun shadows from every side**: under Minecraft shader packs, walls and floors cast sun shadow whichever way they face the camera, and chunks fading in cast whole shadows instead of dithered ones.

### Sky & weather

- **Sun moves steadily under shader packs**: under Minecraft shader packs the sun moves evenly from sunrise to sunset instead of holding still around noon in summer and jumping across the sky at noon in winter.
- **Dense fog keeps the near world**: at the densest fog the fog ends no closer than the near world, instead of Viewpoint pulling it in to 40 m at once.

### World

- **Chunks fade in without holes**: under Minecraft shader packs, a chunk fading in is dithered alike in the near and the far world, so no holes show the sky for half a second.
- **Far buildings in their own colour**: under Minecraft shader packs, packs that read the texture get the colour of far buildings and floors once instead of twice, so they no longer draw them much darker.
- **Floors appear at once**: floors with a new bake appear at once instead of dithering in over half a second with nothing behind them.
- **Flat pack models without flicker**: neighbouring rugs and other flat model pack pieces are drawn a hair apart in depth, so their overlaps no longer flicker with every camera step; applies to chunks built after the change.
- **Garage doors in front of the wall**: each piece of a metal garage door is drawn a hair in front of the wall and of its neighbours, so it no longer flickers with every camera step; applies to chunks built after the change.
- **3D corpses between camera and character**: in third person, 3D corpses are picked from the camera instead of from where the character stands, so corpses between the two stay on screen.

### Interface

- **Game outlines under shader packs**: under Minecraft shader packs, the game's outlines on characters show again, such as the red one on an enemy in reach.

### Always on

Without a switch: a shader pack chosen in the main menu loads its relief textures, and the built-in library pack's lamp options show while a shader pack is in use.

## Armaku's Tweaks

My own preferences and additions.

### On foot

- **Quick body turn**: walking, the body turns to where it goes within Viewpoint's Body turn smoothing, so a wide turn is as quick as a small one, instead of at the game's fixed rate.
- **Zoom with the mouse wheel**: the wheel moves the camera nearer and farther along the view, on foot and in a vehicle, each with its own nearest and farthest zoom; shoulder and height stay where Viewpoint's settings put them.
- **Zoom in indoors**: on foot, entering a building zooms the camera in, never out, and leaving zooms it back; the mouse wheel indoors moves a zoom of its own.

### Driving

- **Looking down**: sets how far the camera looks down when it swings in behind the vehicle.
- **Swing delay**: sets how long the mouse rests before the camera swings back in behind the moving vehicle, where Viewpoint waits 0.75 s.
- **Free look in the vehicle**: held while seated, Viewpoint's free look key keeps the camera where you look instead of swinging it back in behind the vehicle; letting go swings it back at once.

### Interface

- **Open the 3D view in third person**: switching from the isometric view to Viewpoint's view always lands in third person, on foot and in a vehicle, instead of the view you left it in.
- **Free the cursor in menus**: context menus, radial menus and windows such as the map free the cursor while they are open.
- **Hide the loot menu behind menus**: Viewpoint's loot menu steps aside while a context or radial menu is open.
- **Loot window key opens the inventory**: without a loot menu to act on, Viewpoint's loot window key opens your inventory with the cursor free; again to close it.

### Graphics

- **Rain and snow under shader packs**: under Minecraft shader packs, rain falls in streaks and snow drifts in flakes around you, slanted by the wind, in place of Viewpoint's rain and snow.
- **Game weather for shader packs**: hands the game's overcast, cloud cover, gloom, sun strength, fog range and weather to shader packs written to read them (vpOvercast, vpCloudCover, vpDesaturation, vpSunStrength, vpFogStart, vpFogEnd, vpWeather).
- **Plants turned at random**: each grass tuft, bush and small plant is turned by its own random angle, its shadow with it, so a field no longer shows a pattern.
- **Game puddles for shader packs**: hands the game's puddle and wet ground levels and Viewpoint's rain textures to shader packs written to read them (vpPuddles, vpWetGround, vpRain, vpRainClock, vpRainTextures).
- **No occlusion cloud around your character**: hands where your character stands to shader packs written to read it (vpPlayer), so their screen-space ambient occlusion leaves your body out.
- **Sky darkens with the game's night**: the sun rises and sets when street lights switch off and on, and sinks below the horizon as the game's night darkens, so the sky matches the game's day in every season.
- **Lamp reach**: sets how far lamps light with Viewpoint's per-pixel lamps and cast shadows, where Viewpoint uses 24 m.

## First start

On first start the mod sets Viewpoint's camera side offset to 0 and its own vehicle camera tilt (Looking down) to 15°, once, toward the camera of my earlier mod [Third Person Camera for Project Viewpoint](https://github.com/armakupub/ViewpointThirdPerson). Change them freely after.

## Removing the mod

Set Viewpoint's "Camera side offset" (Controls / Third person) back to its default of 0.25 if you want Viewpoint's own framing again. Everything else goes with the mod: the fixes' and tweaks' values are its own settings, and Viewpoint ignores them once the mod is gone. Viewpoint's switches for third person on foot and in a vehicle stay as last used.

## Measurements

Every drawn frame while turning in third person at about 175 fps, 95th percentile, measured with Viewpoint 0.1.6:

| In curves                          | Without fix | With fix |
|------------------------------------|------------:|---------:|
| Vehicle twitch against the camera  |       0.47° |    0.03° |
| Camera shake in the world          |      3.5 cm |  0.36 cm |
| Vehicle shake in the world         |      3.7 cm |  0.38 cm |

## Requirements

- Project Zomboid B42.21
- [ZombieBuddy](https://steamcommunity.com/sharedfiles/filedetails/?id=3619862853)
- Project Viewpoint

## Building

Run `bash build.sh`. It compiles against Project Zomboid, ZombieBuddy and Viewpoint and installs the mod into `~/Zomboid/mods/ArmakuViewpointFixes`. Paths can be overridden with `PZ_DIR`, `VIEWPOINT_JAR` and `JDK_DIR`.

## License

MIT
