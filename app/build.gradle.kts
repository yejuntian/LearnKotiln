plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.study.learnkotiln"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.study.learnkotiln"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            buildConfigField("Boolean", "DEBUG_MODE", "true")
        }
        getByName("release") {
            buildConfigField("Boolean", "DEBUG_MODE", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    //添加dataBing库支持
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    //noinspection GradleCompatible
    implementation(deps.androidx.coreKtx)
    implementation(deps.androidx.appCompat)
    implementation(deps.androidx.material)
    implementation(deps.androidx.constraintLayout)
    testImplementation(deps.test.junit)
    androidTestImplementation(deps.test.androidx.junit)
    androidTestImplementation(deps.test.androidx.espresso.core)

    // Retrofit 核心库
    implementation(deps.retrofit.retrofit_core)
    // Retrofit Gson 转换器
    implementation(deps.retrofit.retrofit_converter)
    //okhttp3
    implementation(deps.okhttp3.okhttp)
    //OkHttp3 的日志拦截器
    implementation(deps.okhttp3.logging_interceptor)

    // 核心协程库
    implementation(deps.coroutines.core)
    // 协程库支持Android
    implementation(deps.coroutines.android)
    // 如果你希望在测试中使用协程
    testImplementation(deps.coroutines.test)

    //ViewModel组件 Kotlin支持 viewModelScope 的版本
    implementation(deps.lifecycle.viewModelKtx)
    //LiveData组件
    implementation(deps.lifecycle.liveDataKtx)
    //使用 LiveData 的扩展函数
    implementation(deps.lifecycle.runtimeKtx)

    //Kotlin 反射库提供了运行时反射能力
    implementation(deps.test.kotlin_reflect)

    // Room components
    implementation(deps.room.room_runtime)
    //注解处理器处理 @Entity, @Dao, @Database 等注解，并生成 Room 数据库的相关代码，例如实体的 _Impl 类和数据库访问对象（DAO）的实现
    //noinspection KaptUsageInsteadOfKsp
    kapt(deps.room.room_compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(deps.room.room_ktx)

    // Navigation components
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation(mviBase)
    implementation(viewpager2)
}