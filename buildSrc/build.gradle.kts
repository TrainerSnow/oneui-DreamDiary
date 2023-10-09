plugins {
    `kotlin-dsl`
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.kapt) apply false
}

repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
    gradlePluginPortal()
}
