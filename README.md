# Xiaokeer App

[![Android CI](https://github.com/xiaokeer/xiaokeer-app/actions/workflows/android-ci.yml/badge.svg)](https://github.com/xiaokeer/xiaokeer-app/actions/workflows/android-ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

一个基于 Kotlin Multiplatform 和 Compose Multiplatform 的移动应用，旨在帮助用户提高生活质量和工作效率。

## 项目简介

Xiaokeer App 是 xiaokeer 全生态系列项目中的移动设备应用部分，它将与 xiaokeer-service 进行数据交互，实现各种功能特性，为用户提供全方位的生活和工作辅助。

## 技术栈

| 组件 | 技术 |
|------|------|
| 语言 | Kotlin 2.3.0 |
| UI 框架 | Compose Multiplatform 1.10.0 |
| 构建工具 | Gradle (Kotlin DSL) |
| Android Min SDK | 24 (Android 7.0) |
| Android Target SDK | 36 |

## 项目结构

```
xiaokeer-app/
├── composeApp/                 # 主应用模块
│   └── src/
│       ├── commonMain/         # 跨平台共享代码
│       └── androidMain/        # Android 平台特定代码
├── gradle/
│   └── libs.versions.toml      # 依赖版本管理
└── .github/
    └── workflows/              # CI/CD 配置
```

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Android Studio Hedgehog (2023.1.1) 或更高版本
- Android SDK (API Level 24+)

### 构建项目

**构建 Debug APK:**

```bash
# macOS/Linux
./gradlew :composeApp:assembleDebug

# Windows
.\gradlew.bat :composeApp:assembleDebug
```

**构建 Release APK:**

```bash
# macOS/Linux
./gradlew :composeApp:assembleRelease

# Windows
.\gradlew.bat :composeApp:assembleRelease
```

**清理项目:**

```bash
# macOS/Linux
./gradlew clean

# Windows
.\gradlew.bat clean
```

### APK 输出位置

- Debug APK: `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
- Release APK: `composeApp/build/outputs/apk/release/composeApp-release-unsigned.apk`

## 开发指南

### 平台抽象模式

项目使用 Kotlin 的 `expect`/`actual` 机制实现平台特定代码：

- 在 `commonMain/` 中定义 `expect` 声明
- 在 `androidMain/` 中提供 `actual` 实现

### 包结构

所有 Kotlin 代码位于 `com.example.xiaokeer_app` 包下。

## CI/CD

项目配置了 GitHub Actions 自动化构建：

- **push/PR 到 main/master/develop**: 自动构建 Debug APK
- **push 到 main/master**: 额外构建 Release APK
- 构建产物自动上传为 Artifacts

## 许可证

本项目采用 [MIT License](LICENSE) 开源许可证。

## 相关项目

- xiaokeer-service - 后端服务项目

## 参与贡献

欢迎提交 Issue 和 Pull Request 来帮助改进项目。

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
