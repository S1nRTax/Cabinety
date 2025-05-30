plugins {
    alias(libs.plugins.android.application)
    id("io.freefair.lombok") version "8.4"
    id("androidx.navigation.safeargs")  // Java version (no .kotlin suffix)
}

android {
    namespace = "com.example.cabinetmedical"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cabinetmedical"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
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
    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.navigation.runtime.android)  // Ensure this is the Java version
    annotationProcessor(libs.room.compiler)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    // Phone number
    implementation(libs.libphonenumber)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Navigation (Java)
    implementation("androidx.navigation:navigation-fragment:2.7.7")  // Java version
    implementation("androidx.navigation:navigation-ui:2.7.7")       // Java version
}