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
    }
}

dependencies {

    //noinspection GradleCompatible
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit 核心库
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit Gson 转换器
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    //OkHttp3 的日志拦截器
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")


    // 核心协程库
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    // 协程库支持Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    // 如果你希望在测试中使用协程
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    //ViewModel组件 Kotlin支持 viewModelScope 的版本
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //LiveData组件
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //使用 LiveData 的扩展函数
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    //Kotlin 反射库提供了运行时反射能力
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    //注解处理器处理 @Entity, @Dao, @Database 等注解，并生成 Room 数据库的相关代码，例如实体的 _Impl 类和数据库访问对象（DAO）的实现
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.6.1")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // Navigation components
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation(project(":lib_viewpager2"))
}