// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false // Added from groovy file
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false

    alias(libs.plugins.ksp) apply false
    //alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android) apply false // Added from groovy file
    alias(libs.plugins.google.services) apply false // Added from groovy file
}


// Clean task configuration
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
