plugins {
    id 'com.android.application' apply false
    id 'com.android.library' apply false
    id 'org.jetbrains.kotlin.android' apply false
    id 'com.google.dagger.hilt.android' apply false
    id 'com.google.gms.google-services' apply false
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
