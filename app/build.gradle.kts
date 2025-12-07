plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin") // 🧭 Safe Args plugin shu yerda
}

android {
    namespace = "com.example.mazali"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mazali"
        minSdk = 31
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
// viewModelScope uchun
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
// coroutine uchun

    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Or the latest stable version
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")// Or the latest stable version
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // OkHttp Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1") // Or the latest stable version
    // Gson (if not already included by converter-gson)
    implementation("com.google.code.gson:gson:2.10.1") // Or the latest stable version

    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx")

    implementation("com.google.android.gms:play-services-location:21.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // 📦 Lottie animatsiya
    implementation("com.airbnb.android:lottie:6.3.0")

    // 🧭 Splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // 🧭 RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    // 🧭 Navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
