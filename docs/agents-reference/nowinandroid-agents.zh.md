# Now in Android AGENTS.md 中文意译

> 来源：https://github.com/android/nowinandroid/blob/main/AGENTS.md  
> 说明：这是面向学习和对照的中文意译/结构化整理，不是逐字官方译文。

## 项目概览

Now in Android 是一个使用 Kotlin 编写的原生 Android 移动应用，用于定期展示 Android 开发相关资讯。用户可以关注主题、在有新内容时接收通知，并收藏内容。

## 架构

该项目是一个现代 Android 应用，遵循 Google 官方 Android 架构指南。它是响应式的单 Activity 应用，主要使用以下技术和模式：

- **UI**：完全使用 Jetpack Compose 构建，包括 Material 3 组件，并支持不同屏幕尺寸的自适应布局。
- **状态管理**：使用 Kotlin Coroutines 和 `Flow` 实现单向数据流。`ViewModel` 作为状态持有者，对外暴露 UI 状态数据流。
- **依赖注入**：全应用使用 Hilt 管理依赖，简化依赖组织并提升可测试性。
- **导航**：使用 Compose 版 Jetpack Navigation 2，以声明式、类型安全的方式在页面之间导航。
- **数据层**：使用 Repository 模式。
  - **本地数据**：使用 Room 和 DataStore 持久化数据。
  - **远程数据**：使用 Retrofit 和 OkHttp 请求网络数据。
- **后台任务**：使用 WorkManager 处理可延迟执行的后台任务。

## 模块结构

- 主 Android App 位于 `app/`。
- Feature 模块位于 `feature/`。
- Core 和共享模块位于 `core/`。

## 构建与测试命令

该应用和 Android library 模块包含两个 product flavor：`demo`、`prod`；两个 build type：`debug`、`release`。

- 构建：`./gradlew assemble{Variant}`，常用 `assembleDemoDebug`。
- 修复 lint / 格式问题：`./gradlew spotlessApply`。
- 运行本地测试：`./gradlew {variant}Test`。
- 运行单个测试：`./gradlew {variant}Test --tests "com.example.myapp.MyTestClass"`。
- 运行本地截图测试：`./gradlew verifyRoborazziDemoDebug`。

## 仪器测试

通过 Gradle-managed devices 运行设备测试，例如：

- `./gradlew pixel6api31aospDebugAndroidTest`
- `./gradlew pixel4api30aospatdDebugAndroidTest`
- `./gradlew pixelcapi30aospatdDebugAndroidTest`

## 测试编写规则

### 仪器测试

- UI feature 测试只使用 `ComposeTestRule` 搭配 `ComponentActivity`。
- 更大的测试放在 `:app` 模块，可以启动 `MainActivity` 这类 Activity。

### 本地测试

- 大多数断言使用 `kotlinx.coroutines` 相关测试能力。
- 复杂协程测试使用 Turbine。
- 普通断言使用 Truth。

## 持续集成

- CI workflow 定义在 `.github/workflows/*.yaml`。
- 截图测试由 CI 生成，不应从本地开发机提交到仓库。

## 版本控制和代码位置

- 项目使用 Git，并托管在 GitHub。
