// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}

plugins {
    alias(libs.plugins.android - application) apply false
    alias(libs.plugins.kotlin - android) apply false
    alias(libs.plugins.kotlin - kapt) apply false
    alias(libs.plugins.hilt.android) apply false
}

// Clean task configuration
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}