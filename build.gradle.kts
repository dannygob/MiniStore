plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.ksp) apply false
//    kotlin("KSP")
    alias(libs.plugins.googleservices) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
