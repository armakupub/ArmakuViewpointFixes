# Armaku's Fixes for Project Viewpoint

My personal fixes and tweaks for [Project Viewpoint](https://steamcommunity.com/sharedfiles/filedetails/?id=3809306528) in Project Zomboid B42.

- **Smooth driving**: your vehicle, what it tows and everyone in it move smoothly at any frame rate, so the view no longer judders while driving.
- **Steady camera on foot**: the third-person camera no longer shakes while walking at uneven frame rates.

## Measurements

Viewpoint's camera trace while driving in third person at 115 to 145 fps:

| While driving                      | Without fix | With fix |
|------------------------------------|------------:|---------:|
| Frames without movement            |      28.4 % |      0 % |
| Deviation per frame (RMS)          |     5.95 cm |  2.28 cm |
| Speed jump frame to frame (median) |    11.3 m/s | 0.77 m/s |

## Requirements

- Project Zomboid B42.21
- [ZombieBuddy](https://steamcommunity.com/sharedfiles/filedetails/?id=3619862853)
- Project Viewpoint

## Building

Run `bash build.sh`. It compiles against Project Zomboid, ZombieBuddy and Viewpoint and installs the mod into `~/Zomboid/mods/ArmakuViewpointFixes`. Paths can be overridden with `PZ_DIR`, `VIEWPOINT_JAR` and `JDK_DIR`.

## License

MIT
