# NoiceBoard

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![API 24+](https://img.shields.io/badge/API-24%2B-brightgreen)](https://developer.android.com/about/versions/nougat)
[![Target SDK 35](https://img.shields.io/badge/Target%20SDK-35-blue)](https://developer.android.com/about/versions/15)
[![Java 11](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.9.2-02303A?logo=gradle&logoColor=white)](https://gradle.org)
[![Material Design 3](https://img.shields.io/badge/Material%20Design-3-757575?logo=materialdesign&logoColor=white)](https://m3.material.io)
[![AndroidX](https://img.shields.io/badge/AndroidX-Enabled-6200EE)](https://developer.android.com/jetpack/androidx)
[![ViewBinding](https://img.shields.io/badge/ViewBinding-Enabled-4CAF50)](https://developer.android.com/topic/libraries/view-binding)
[![Version](https://img.shields.io/badge/Version-0.1.9-orange)](https://github.com/pepperonas/noice-board/releases)
[![GitHub](https://img.shields.io/github/license/pepperonas/noice-board?color=blue)](LICENSE)
[![GitHub repo size](https://img.shields.io/github/repo-size/pepperonas/noice-board)](https://github.com/pepperonas/noice-board)
[![GitHub last commit](https://img.shields.io/github/last-commit/pepperonas/noice-board)](https://github.com/pepperonas/noice-board/commits/main)
[![GitHub issues](https://img.shields.io/github/issues/pepperonas/noice-board)](https://github.com/pepperonas/noice-board/issues)
[![GitHub stars](https://img.shields.io/github/stars/pepperonas/noice-board?style=social)](https://github.com/pepperonas/noice-board)

A soundboard Android app with categorized sound buttons, video playback, and sharing functionality. Sound categories are auto-discovered from asset folders — just drop files in and rebuild.

## Features

- 5 sound categories with 395 sounds, auto-discovered from asset folders
- 11 videos with fullscreen playback (MP4, WebM, MKV)
- Long-press to share sounds and videos (WhatsApp-compatible)
- Dark/Light theme toggle with persistence
- Bottom Navigation for quick category switching (up to 5 tabs)
- Material Design 3 UI

## Sound Categories

| Category | Sounds | Description |
|----------|--------|-------------|
| Admin Plugin | 262 | Admin & plugin sounds |
| Jngles | 71 | Jingles & short tunes |
| Dart | 24 | Dart game announcements |
| Connect | 21 | Connection sounds |
| Quake | 17 | Quake arena sounds |

## Build

```bash
git clone https://github.com/pepperonas/noice-board.git
cd noice-board
./gradlew assembleDebug
./gradlew installDebug
```

## Adding Content

**Sounds** — Drop `.mp3` or `.wav` files into a folder under `app/src/main/assets/` and rebuild. New folders automatically become new categories.

**Videos** — Drop `.mp4`, `.webm`, or `.mkv` files into `app/src/main/assets/video/` and rebuild.

## Tech Stack

| Component | Detail |
|-----------|--------|
| Language | Java 11 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 (Android 15) |
| UI | Material Design 3 + AndroidX |
| Build | Gradle 8.9.2 |
| Binding | ViewBinding |

## Developer

**Martin Pfeffer** — [celox.io](https://celox.io)

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
