# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Android
.\gradlew.bat :composeApp:assembleDebug      # Debug APK
.\gradlew.bat :composeApp:assembleRelease    # Release APK

# Clean
.\gradlew.bat clean
```

## Architecture

This is a Kotlin Multiplatform (KMP) project using Compose Multiplatform for shared UI across Android

### Module Structure

- **composeApp/** - Main shared module containing all Kotlin code
  - `commonMain/` - Shared code for all platforms (UI, business logic)
  - `androidMain/` - Android-specific implementations
- **iosApp/** - iOS native wrapper (SwiftUI entry point that hosts Compose UI)

### Platform Abstraction Pattern

Uses Kotlin's `expect`/`actual` mechanism for platform-specific code:
- Define `expect` declarations in `commonMain/`
- Provide `actual` implementations in `androidMain/` 

Example: `Platform.kt` (expect) → `Platform.android.kt` and `Platform.ios.kt` (actual)

### Key Technologies

| Component | Technology |
|-----------|------------|
| Language | Kotlin 2.3.0 |
| UI | Compose Multiplatform 1.10.0 |
| Build | Gradle with Kotlin DSL |
| Android Min SDK | 24 |
| Android Target SDK | 36 |

### Package Structure

All Kotlin code lives under `com.example.xiaokeer_app`

### Dependencies

Managed via Gradle Version Catalog in `gradle/libs.versions.toml`
