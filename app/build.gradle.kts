import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.licenses)
}

android {
    namespace = "com.snow.diary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.snow.diary"
        minSdk = 28
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 5
        versionName = "1.0.0-alpha05"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val propFile = rootProject.file("keystore.properties")
            val props = Properties()
            props.load(propFile.inputStream())

            storeFile = rootProject.file(props["storeFile"]!!)
            storePassword = props["storePassword"] as String
            keyPassword = props["keyPassword"] as String
            keyAlias = props["keyAlias"] as String
        }

        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "zlLoyyyW8M6k3CqcEb"
            keyPassword = "zlLoyyyW8M6k3CqcEb"
            keyAlias = "signingkey"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.com.google.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.android.compiler)

    implementation(libs.core.ktx)
    implementation(platform(libs.compose.bom))

    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.biometric)

    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowsizeclas)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.animation)
    implementation(libs.compose.navigation)

    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.compose.lifecycle.runtime)

    implementation(project(":feature:dreams"))
    implementation(project(":feature:persons"))
    implementation(project(":feature:locations"))
    implementation(project(":feature:relations"))
    implementation(project(":feature:preferences"))
    implementation(project(":feature:export"))
    implementation(project(":feature:statistics"))
    implementation(project(":feature:search"))
    implementation(project(":feature:importing"))
    implementation(project(":feature:appinfo"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(libs.oneui)
}