@file:Suppress("unused", "ClassName", "SpellCheckingInspection")

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

const val ktlintVersion = "1.0.0"
const val kotlinVersion = "1.9.23"

object appConfig {
    const val applicationId = "com.study.learnkotiln"

    const val compileSdkVersion = 34
    const val buildToolsVersion = "34.0.0"
    const val minSdkVersion = 21
    const val targetSdkVersion = 33

    private const val MAJOR = 2
    private const val MINOR = 1
    private const val PATCH = 1
    const val versionCode = MAJOR * 10000 + MINOR * 100 + PATCH
    const val versionName = "$MAJOR.$MINOR.$PATCH-SNAPSHOT"
}

object deps {
    object androidx {
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"
        const val coreKtx = "androidx.core:core-ktx:1.12.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.3.2"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
        const val material = "com.google.android.material:material:1.11.0"
        const val startup = "androidx.startup:startup-runtime:1.1.1"
    }

    object lifecycle {
        private const val version = "2.7.0"

        //ViewModel组件 Kotlin支持 viewModelScope 的版本
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"

        //用于在 Android 应用中管理组件的生命周期，观察生命周期事件、处理生命周期变化等
        const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"

        //LiveData组件
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
    }

    object squareup {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.10.0"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:2.10.0"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.12.0"
        const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:1.15.1"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.13"
    }

    object coroutines {
        private const val version = "1.8.0"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object koin {
        private const val version = "3.5.3"

        const val core = "io.insert-koin:koin-core:$version"
        const val android = "io.insert-koin:koin-android:$version"
        const val testJunit4 = "io.insert-koin:koin-test-junit4:$version"
        const val test = "io.insert-koin:koin-test:$version"
    }

    object retrofit {
        private const val version = "2.9.0"

        // Retrofit 核心库
        const val retrofit_core = "com.squareup.retrofit2:retrofit:$version"

        // Retrofit Gson 转换器
        const val retrofit_converter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object okhttp3 {
        private const val version = "4.12.0"

        //okhttp3
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"

        //OkHttp3 的日志拦截器
        const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object room {
        private const val version = "2.6.1"

        // Room components
        const val room_runtime = "androidx.room:room-runtime:$version"

        //注解处理器处理 @Entity, @Dao, @Database 等注解，并生成 Room 数据库的相关代码，例如实体的 _Impl 类和数据库访问对象（DAO）的实现
        //noinspection KaptUsageInsteadOfKsp
        const val room_compiler = "androidx.room:room-compiler:$version"

        // optional - Kotlin Extensions and Coroutines support for Room
        const val room_ktx = "androidx.room:room-ktx:$version"
    }


    const val coil = "io.coil-kt:coil:2.6.0"
    const val viewBindingDelegate = "com.github.hoc081098:ViewBindingDelegate:1.4.0"
    const val flowExt = "io.github.hoc081098:FlowExt:0.8.1-Beta"
    const val timber = "com.jakewharton.timber:timber:5.0.1"

    object arrow {
        private const val version = "1.2.3"
        const val core = "io.arrow-kt:arrow-core:$version"
    }

    object test {
        const val junit = "junit:junit:4.13.2"

        object androidx {
            const val core = "androidx.test:core-ktx:1.5.0"
            const val junit = "androidx.test.ext:junit-ktx:1.1.5"

            object espresso {
                const val core = "androidx.test.espresso:espresso-core:3.5.1"
            }
        }

        const val mockk = "io.mockk:mockk:1.13.10"
        const val kotlinJUnit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"

        //Kotlin 反射库提供了运行时反射能力
        const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:1.9.22"
    }
}

private typealias PDsS = PluginDependenciesSpec
private typealias PDS = PluginDependencySpec

inline val PDsS.androidApplication: PDS get() = id("com.android.application")
inline val PDsS.androidLib: PDS get() = id("com.android.library")
inline val PDsS.kotlinAndroid: PDS get() = id("kotlin-android")
inline val PDsS.jetbrainsKotlinAndroid: PDS get() = id("org.jetbrains.kotlin.android")
inline val PDsS.kotlin: PDS get() = id("kotlin")
inline val PDsS.kotlinKapt: PDS get() = id("kotlin-kapt")
inline val PDsS.kotlinParcelize: PDS get() = id("kotlin-parcelize")
inline val PDsS.pokoPlugin: PDS get() = id("dev.drewhamilton.poko")


inline val DependencyHandler.mviBase get() = project(":mvi")
inline val DependencyHandler.viewpager2 get() = project(":lib_viewpager2")

fun DependencyHandler.addUnitTest(testImplementation: Boolean = true) {
    val configName = if (testImplementation) "testImplementation" else "implementation"

    add(configName, deps.test.junit)
    add(configName, deps.test.mockk)
    add(configName, deps.test.kotlinJUnit)
    add(configName, deps.coroutines.test)
}

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"
