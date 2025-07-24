plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp) // Assuming KSP is needed based on Room and Hilt usage
    alias(libs.plugins.google.services)
    // Removed redundant plugin declarations
}

android {
    namespace = "com.example.ministore" // Harmonized namespace
    compileSdk = 35 // Using higher compileSdk from groovy file

    defaultConfig {
        applicationId = "com.example.ministore" // Harmonized application ID
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room schema location
        ksp {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
        jvmTarget = "17" // Using higher jvmTarget from kts file
        languageVersion = "1.9" // Using languageVersion from kts file
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15" // Using version from kts file
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // Keep specific version for now

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Firebase
    // Removed redundant and commented-out Firebase declarations
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    // Note: firebase.firestore.ktx and firebase.storage.ktx in groovy file are covered by the bom and the non-ktx versions here

    // Coroutines
    implementation(libs.kotlinx.coroutines.android) // Assuming this is in libs.versions.toml
    implementation(libs.kotlinx.coroutines.play.services) // Assuming this is in libs.versions.toml

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx) // Assuming this is in libs.versions.toml

    // Retrofit & OkHttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // Image Loading
    implementation(libs.coil.compose)

    // DataStore
    implementation(libs.datastore.preferences)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose) // Already declared above

    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Assuming this is in libs.versions.toml

    // Barcode Scanner
    implementation("com.google.zxing:core:3.5.2") // Keep specific version for now

    // Accompanist
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4) // Already declared above
    debugImplementation(libs.androidx.ui.tooling) // Already declared above
    debugImplementation(libs.androidx.ui.test.manifest) // Already declared above

    // Note: androidx.compose.compiler and material3 are declared multiple times, consolidated here.
}
