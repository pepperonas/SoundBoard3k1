# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

NoiceBoard is an Android soundboard app (Java) with categorized sound buttons and video playback. Package: `io.celox.soundboard3k1`. Current version: 0.1.9.

## Build Commands

```bash
./gradlew assembleDebug          # Debug APK
./gradlew assembleRelease        # Release APK (uses debug signing)
./gradlew installDebug           # Install on connected device
./gradlew test                   # Unit tests
./gradlew connectedAndroidTest   # Instrumented tests (needs device/emulator)
./gradlew lint                   # Lint check
./gradlew clean                  # Clean build
```

## Architecture

### Navigation Pattern

The app uses **manual FragmentTransaction** (not Navigation Component). `MainActivity` builds `BottomNavigationView` menu items dynamically at runtime based on discovered asset folders. Fragment switching is done via `getSupportFragmentManager().beginTransaction().replace()` into a FrameLayout (`nav_host_fragment_activity_main`). There is a `mobile_navigation.xml` and `ui/home|dashboard|notifications` packages left over from the Android Studio template — these are **unused dead code**.

### Content Discovery

Sound categories are auto-discovered at runtime: `MainActivity.loadSoundCategories()` iterates all top-level asset folders, checks if the first file ends in `.mp3`/`.wav`, and creates a `SoundCategory` for each match. The `video/` folder is explicitly skipped. BottomNavigationView is limited to 5 items (4 sound categories + 1 video tab).

### Media Playback

- **Sound**: `SoundPlayer` wraps a single `MediaPlayer` instance — playing a new sound stops the previous one. Each `SoundAdapter` owns its own `SoundPlayer` instance, released in `SoundFragment.onDestroy()`.
- **Video**: `VideoPlayerActivity` uses `SurfaceView` + `MediaPlayer` for fullscreen playback from assets. Launched via intent extras `video_path` and `video_name`. Auto-finishes on completion.

### Sharing (Long Press)

Both `SoundAdapter` and `VideoAdapter` support long-press to share: asset files are copied to `context.getCacheDir()` subdirectories (`audio/` or `videos/`), then shared via `FileProvider` (`${applicationId}.fileprovider`) with `ACTION_SEND`. Video filenames are sanitized for WhatsApp compatibility.

### Asset Structure

Sound and video files live in `app/src/main/assets/`:
- Sound folders: `admin_plugin/`, `connect/`, `dart/`, `jngles/`, `quake/` (MP3, WAV)
- Video folder: `video/` (MP4, WebM, MKV)
- Adding content: drop files into the appropriate folder and rebuild

### Key Conventions

- **ViewBinding** enabled — all layouts accessed via generated binding classes
- Folder names become display names: underscores replaced with spaces, first letter capitalized
- Sound filenames become button labels: extension stripped, underscores/hyphens → spaces, title-cased
- Theme (light/dark) persisted in SharedPreferences (`theme_prefs` / `night_mode`)
- Release builds use debug signing config (no separate release keystore)
- APK output: `NoiceBoard-{versionName}.apk`

## Development Notes

- Target SDK: 35, Min SDK: 24, Java 11
- Gradle version catalog (`libs.versions.toml`) for dependency management
- UI strings are in German (share dialogs, error messages)
