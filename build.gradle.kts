plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.ksp) apply false
    id ("com.google.dagger.hilt.android") version "2.52" apply false
    id ("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath (libs.hilt.android.gradle.plugin)
    }
}