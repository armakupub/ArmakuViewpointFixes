#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
PZ_DIR="${PZ_DIR:-C:/Games/Steam/steamapps/common/ProjectZomboid}"
VIEWPOINT_JAR="${VIEWPOINT_JAR:-C:/Games/Steam/steamapps/workshop/content/108600/3809306528/mods/Viewpoint/42/media/java/client/Viewpoint.jar}"
JDK_DIR="${JDK_DIR:-C:/Program Files/Zulu/zulu-25}"
INSTALL="${INSTALL:-$USERPROFILE/Zomboid/mods/ArmakuViewpointFixes}"
VERSION_DIR="42.21"
BUILD="$ROOT/build"

if tasklist //FI "IMAGENAME eq ProjectZomboid64.exe" //FO CSV //NH 2>/dev/null | grep -qi ProjectZomboid64; then
    echo "[build] ERROR: Project Zomboid is running. Close it before building." >&2
    exit 1
fi

"$JDK_DIR/bin/java.exe" "$ROOT/tools/AdviceAccessCheck.java" "$ROOT/src"

rm -rf "$BUILD"
mkdir -p "$BUILD/classes"
mapfile -t SOURCES < <(find "$ROOT/src" -name '*.java')
"$JDK_DIR/bin/javac.exe" --release 17 \
    -classpath "$PZ_DIR/projectzomboid.jar;$PZ_DIR/ZombieBuddy.jar;$VIEWPOINT_JAR" \
    -d "$BUILD/classes" "${SOURCES[@]}"
"$JDK_DIR/bin/jar.exe" --create --file "$BUILD/armakuviewpointfixes.jar" -C "$BUILD/classes" .

STAGE="$BUILD/stage/ArmakuViewpointFixes"
mkdir -p "$STAGE/$VERSION_DIR/media/java/client"
cp "$ROOT/mod_files/mod.info" "$STAGE/mod.info"
cp "$ROOT/mod_files/$VERSION_DIR/mod.info" "$STAGE/$VERSION_DIR/mod.info"
cp "$BUILD/armakuviewpointfixes.jar" "$STAGE/$VERSION_DIR/media/java/client/armakuviewpointfixes.jar"

rm -rf "$INSTALL"
cp -r "$STAGE" "$INSTALL"
echo "[build] Installed to $INSTALL"
