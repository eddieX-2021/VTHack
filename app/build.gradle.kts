plugins {
    // Apply the Android application plugin only once
    alias(libs.plugins.android.application)
    
    // Apply the Kotlin Android plugin
    alias(libs.plugins.kotlin.android)

    // Apply Google Services plugin for Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.foodscanner.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.foodscanner.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.camera.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Optional for UI testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")


    // Firebase BoM and Analytics
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Firebase services you want to use
    implementation("com.google.firebase:firebase-database")      // Realtime Database
    implementation("com.google.firebase:firebase-auth")

    // ML Kit dependencies
    implementation("com.google.mlkit:image-labeling:17.0.9")
    implementation("com.google.android.gms:play-services-mlkit-image-labeling:16.0.8")
    implementation("com.google.mlkit:labeling:18.1.0")

    // Android CameraX dependencies
    implementation("androidx.camera:camera-core:1.2.0")
    implementation("androidx.camera:camera-camera2:1.2.0")
    implementation("androidx.camera:camera-lifecycle:1.2.0")
}
