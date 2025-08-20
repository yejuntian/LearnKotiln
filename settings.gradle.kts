pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
    }
}

rootProject.name = "LearnKotiln"
include(":app")
include(":lib_viewpager2")
include(":mvi")

//添加flutter 子模块
val flutterGroovy = File(rootDir, "flutter_zero/.android/include_flutter.groovy")
if (flutterGroovy.exists()) {
    apply(from = flutterGroovy)
}
include(":learncompose")
