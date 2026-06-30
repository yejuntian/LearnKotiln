# Androidify AGENTS.md 中文意译

> 来源：https://github.com/android/androidify/blob/main/AGENTS.md  
> 说明：这是面向学习和对照的中文意译/结构化整理，不是逐字官方译文。

## 项目概览

Androidify 是一个 Android 应用，允许用户使用 AI 创建自定义 Android 机器人头像。项目使用现代 Android 技术，并集成 Google Gemini 模型实现生成式 AI 功能。

## 技术栈

- **语言**：Kotlin。
- **UI 框架**：Jetpack Compose。
- **架构**：MVVM / Clean Architecture 风格，项目通过 `core`、`feature`、`data` 等目录体现分层思想。
- **AI / ML**：
  - Gemini API，通过 Firebase AI Logic SDK 接入。
  - Imagen 模型。
- **相机**：CameraX。
- **导航**：Compose 版 Navigation 3。
- **依赖注入**：Hilt。
- **构建系统**：Gradle Kotlin DSL。

## 项目结构

项目采用模块化架构：

- **`/app`**：主 Android 应用模块，负责连接功能并配置 App。
- **`/core`**：跨模块共享的核心库和工具，例如 `core/network`、`core/util`。
- **`/feature`**：具体功能模块，例如 `feature/home`、`feature/creation`。
- **`/data`**：数据层 Repository 和数据源。
- **`/wear`**：Wear OS 专用模块。
- **`/watchface`**：Wear OS 表盘模块。
- **`/setup`**：项目初始化脚本和配置文件，例如 Firebase Remote Config 配置。

## 开发流程

### 前置条件

- Android Studio，使用最新稳定版；如果项目依赖较新的功能，可以使用 Preview 版本。
- JDK 17 或更高版本。
- `/app` 目录下需要放置 `google-services.json`，具体配置参考 README。

### 常用命令

构建：

```bash
./gradlew assembleDebug
```

测试：

```bash
# 单元测试
./gradlew test

# 仪器测试
./gradlew connectedAndroidTest
```

代码格式和 lint：

```bash
# 检查格式问题
./gradlew spotlessCheck

# 自动修复格式问题
./gradlew spotlessApply
```

## 配置

- **Firebase**：需要有效的 `google-services.json`。
- **Remote Config**：默认配置位于 `core/network/src/main/res/xml/remote_config_defaults.xml`。
- **API Key**：通过 `local.properties` 或 BuildConfig 字段管理；具体查看对应模块的 `build.gradle.kts`。

## 给 AI Agent 的关键规则

1. **Compose 优先**：遵循现代 Jetpack Compose 最佳实践。
2. **模块化**：尊重模块边界，core 模块不应依赖 feature 模块。
3. **Spotless**：尽量对修改过的文件运行 `spotlessApply`，或确保代码符合项目风格。
4. **AI 功能**：修改 AI 相关功能时，参考 Firebase AI Logic SDK 文档，并遵循项目已有实现模式。
5. **大屏适配**：该应用是大屏适配的 Tier 1 示例，修改时应确保在不同尺寸 Android 设备上都能正常工作。
