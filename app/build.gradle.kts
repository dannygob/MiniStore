plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ministore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ministore"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        freeCompilerArgs += listOf("-Xjvm-default=all")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8" // Explicit version from TOML [versions].compose
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.androidx.compose.bom)) // Added for Compose BOM

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0") // version from lifecycleRuntimeKtx
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // version from hiltNavigationCompose

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp("androidx.room:room-compiler:2.7.1") // version from roomCompiler, switched to ksp

    // Firebase (uses BOM)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // version from kotlinxCoroutinesAndroid
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3") // version from kotlinxCoroutinesPlayServices

    // CameraX & Barcode Scanning
    implementation(libs.barcode.scanning) // version from barcodeScanning
    implementation("androidx.camera:camera-camera2:1.4.2")   // version from cameraCamera2
    implementation("androidx.camera:camera-lifecycle:1.4.2") // Assuming same version as camera2
    implementation("androidx.camera:camera-view:1.4.2")      // Assuming same version as camera2

    // Image loading
    implementation(libs.coil.compose)

    // Testing
    testImplementation("junit:junit:4.13.2") // Explicit version, libs.junit was unresolved
    androidTestImplementation("androidx.test.ext:junit:1.2.1") // version from androidxJunit / junitVersion
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1") // version from espressoCoreVersion
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // Uses Compose BOM
    debugImplementation("androidx.compose.ui:ui-tooling") // Uses Compose BOM
    debugImplementation("androidx.compose.ui:ui-test-manifest") // Uses Compose BOM
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}