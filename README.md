# Armaku's Fixes for Project Viewpoint

My personal fixes for [Project Viewpoint](https://steamcommunity.com/sharedfiles/filedetails/?id=3809306528) in Project Zomboid B42, kept until they are fixed in Viewpoint itself.

- **Smooth driving**: your vehicle, what it tows and everyone in it move smoothly at any frame rate, so the view no longer judders while driving.
- **Steady camera on foot**: the third-person camera no longer shakes while walking at uneven frame rates.

## Requirements

- Project Zomboid B42.21
- [ZombieBuddy](https://steamcommunity.com/sharedfiles/filedetails/?id=3619862853)
- Project Viewpoint

## Building

Run `bash build.sh`. It compiles against Project Zomboid, ZombieBuddy and Viewpoint and installs the mod into `~/Zomboid/mods/ArmakuViewpointFixes`. Paths can be overridden with `PZ_DIR`, `VIEWPOINT_JAR` and `JDK_DIR`.

## License

MIT
