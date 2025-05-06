plugins {
    alias(libs.plugins.android.application)
    id("io.freefair.lombok") version "8.4"
}

android {
    namespace = "com.example.cabinetmedical"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cabinetmedical"
        minSdk = 21
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime.android)
    implementation(libs.libphonenumber)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}