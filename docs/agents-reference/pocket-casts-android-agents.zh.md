# Pocket Casts Android AGENTS.md 中文意译

> 来源：https://github.com/Automattic/pocket-casts-android/blob/main/AGENTS.md  
> 说明：这是面向学习和对照的中文意译/结构化整理，不是逐字官方译文。

## 文档用途

该文件用于指导 AI 编码助手在当前仓库中工作。

## 常用命令

### 构建

```bash
# 构建 debug APK，使用 staging 服务器并带 .debug 后缀
./gradlew :app:assembleDebug

# 构建 debugProd APK，开发构建但连接生产服务器
./gradlew :app:assembleDebugProd

# 构建 prototype，开启混淆的预发布构建
./gradlew :app:assemblePrototype

# 构建 release APK
./gradlew :app:assembleRelease

# 安装 debug 构建到连接的设备
./gradlew :app:installDebugProd
```

### 测试

```bash
# 运行 app 模块单元测试
./gradlew :app:testDebugUnitTest

# 运行指定 feature 模块单元测试
./gradlew :modules:features:search:testDebugUnitTest

# 运行 app 仪器测试
./gradlew :app:connectedDebugAndroidTest

# 运行指定模块仪器测试
./gradlew :modules:features:search:connectedDebugAndroidTest
```

### 代码质量

```bash
# 检查代码格式，合并前必须通过
./gradlew spotlessCheck

# 自动格式化代码
./gradlew spotlessApply

# 安装 Git hooks，在 pre-commit 阶段运行 spotless
./gradlew installGitHooks

# 对所有应用模块运行 lint
./gradlew aggregatedLintRelease

# 运行依赖分析
./gradlew buildHealth
```

### 构建变体

- **debug**：开发构建，带 `.debug` 后缀，使用 staging 服务器。
- **debugProd**：开发构建，但指向生产服务器。
- **prototype**：开启 ProGuard / R8 的预发布构建。
- **release**：正式生产构建，开启混淆和资源压缩。

## 架构概览

### 多模块结构

**应用模块**：

- `app/`：主 Android 手机应用。
- `automotive/`：Android Automotive OS 版本。
- `wear/`：Wear OS 版本。

**功能模块**：

- 位于 `modules/features/`。
- 每个 feature 自包含 UI、ViewModel 和功能专属逻辑。
- feature 可以依赖 service，但不能依赖其他 feature。

**服务模块**：

- 位于 `modules/services/`。
- 提供共享基础设施和业务逻辑。
- 典型模块包括：
  - `model`：数据库实体和 Room DAO。
  - `repositories`：数据层，封装服务器和本地 model，体现 Repository 模式。
  - `servers`：网络 API 客户端，使用 Retrofit 和 OkHttp。
  - `compose`：共享 Compose 组件。
  - `ui`：共享 UI 主题和组件。
  - `analytics`：埋点统计。
  - `localization`：字符串和翻译资源。

**依赖方向**：

```text
Applications (app, automotive, wear)
    ↓
Features (account, search, player, etc.)
    ↓
Services (repositories, ui, compose, analytics, etc.)
    ↓
Core Services (model, servers)
```

## 架构模式：MVVM

- **ViewModel**：使用 `@HiltViewModel` 注解，并使用 `StateFlow` 管理状态。
- **UI 状态模式**：使用 sealed interface / sealed class 表示 UI 状态，例如 `Idle`、`Loading`、`Success`、`Error`。
- **单向数据流**：ViewModel 对外暴露不可变状态流，并通过函数接收用户操作。
- **依赖注入**：全项目使用 Hilt / Dagger 2。
- **响应式编程**：新旧策略并存，历史代码使用 RxJava2，现代代码使用 Coroutines / Flow。

## 技术栈

### UI

- 新功能主要使用 Jetpack Compose，并使用 Material 组件。
- 当前代码库的 Compose 组件主要还是 Material2，Material3 迁移进行中。
- 新 Compose UI 应优先使用 Material3，除非所在模块尚未迁移；不确定时先查看模块依赖或咨询团队。
- 旧代码中仍存在 XML View。
- XML 布局启用 View Binding。

### 其他技术

- **依赖注入**：Hilt。
- **数据库**：Room，并具有大量历史 migration。
- **网络**：Retrofit、OkHttp、Moshi。
- **媒体播放**：Media3 / ExoPlayer，以及 Chromecast Cast Framework。
- **图片加载**：Coil。
- **异步**：Coroutines 和 WorkManager。
- **测试**：JUnit、Mockito、Turbine、Compose UI Test、UIAutomator、MockWebServer。

## 开发指南

### Git 工作流

功能开发应从 `main` 创建分支。只有需要把已合并提交 cherry-pick 到 release 分支时，才从 release 分支创建分支。

从 release 分支开发新功能会让后续合并变得混乱：当 release 分支再合回 `main` 时，很难判断哪些提交属于 release，哪些属于无关功能。因此：

- 所有功能分支都从 `main` 创建，即使该变更也需要进入正在进行的 release。
- 如果某个变更也必须进入 release 分支，例如即将发布版本需要的修复：
  1. 先基于 `main` 开发并合并。
  2. 再从 release 分支创建新分支，将相关提交 cherry-pick 过去，并向 release 分支发起 PR。
- cherry-pick 只保留 release 真正需要的提交，避免 release 分支混入无关功能。
- cherry-pick 可能产生冲突，解决冲突后必须认真验证再开 PR：
  - `app`、`automotive`、`wear` 都能构建。
  - `spotlessCheck`、lint 等质量门禁通过。
  - 单元测试和集成测试通过。

### 技术偏好

**新 UI 使用 Jetpack Compose**：

- 所有新 screen / view 必须使用 Compose。
- 修改旧 XML view 时，鼓励在合适情况下迁移到 Compose。
- 使用 `modules/services/compose` 和 `modules/services/ui` 中的 Material3 组件。

**优先使用 Coroutines，而不是 RxJava**：

- 新代码应使用 Coroutines 和 Flow。
- 有合适机会时，将 RxJava 代码迁移到 Coroutines。
- ViewModel 状态管理使用 `StateFlow`。

**使用 Kotlin**：

- 所有新功能必须使用 Kotlin。
- 有合适机会时，将 Java 代码迁移到 Kotlin。

### 代码风格

- 行宽限制：120 字符。
- 合并前必须通过 `spotlessCheck`，可用 `spotlessApply` 自动格式化。
- 临时说明使用 `TODO`，不要使用仓库不允许的 `FIXME`。
- ktlint 会执行 Kotlin 风格检查，并包含 Compose 自定义规则。
- Kotlin 编译中所有 warning 都视为 error。

### 字符串资源

- 英文字符串只添加到 `modules/services/localization`。
- 翻译通过 GlotPress 管理并自动拉取。
- 不需要翻译的字符串使用 `translatable="false"` 标记。

### 图片资源

- 图片只添加到 `modules/services/images`。
- 优先使用矢量图，例如 `.svg`，适合图标、Logo 和简单插画。
- 新图片优先使用 WebP，以获得更好的压缩率和更小 APK 体积。
- PNG 只在必要时使用，例如 launcher icon、小型透明图，或无法修改的第三方资源。

### 测试

- ViewModel、manager 和业务逻辑需要编写单元测试。
- 协程测试使用 `MainCoroutineRule`。
- Flow 测试使用 Turbine。
- 共享测试工具位于 `modules/services/sharedtest`。
- 单元测试放在 `src/test/`。
- 仪器测试放在 `src/androidTest/`。

### Feature Flag

项目使用 `FeatureFlag` 系统进行 A/B 测试和灰度发布。修改带开关控制的功能前，需要先检查相关 feature flag。

### 模块依赖

- feature 可以依赖 service，但不能依赖其他 feature。
- service 模块可以依赖其他 service，例如 `compose` 依赖 `ui`，`repositories` 依赖 `model`。
- 多数 feature 模块依赖 `model`、`repositories`、`ui`、`compose`、`analytics`、`localization`。
- `repositories` 是核心数据访问层。
- 依赖分析插件会通过 `./gradlew buildHealth` 检查这些规则。

## 项目特殊说明

### 数据库迁移

Room 数据库已有大量 migration。修改 entity 时必须：

- 始终提供 migration 路径。
- 保持 schema 导出，schema 位于 `modules/services/model/schemas/`。
- 充分测试 migration。

### 构建变体和服务器地址

- `debug` 使用 staging 服务器。
- `debugProd`、`prototype`、`release` 使用生产服务器。
- 服务器地址通过 `build.gradle.kts` 中的 `buildConfigField` 配置。

### 埋点

使用 `AnalyticsTracker` 服务进行事件追踪，埋点系统集成 Automattic Tracks。
