plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // Dibutuhkan untuk Room Database
}

android {
    namespace = "com.example.smartreturn"
    compileSdk = 35 // Menggunakan SDK 35 (Stable)

    defaultConfig {
        applicationId = "com.example.smartreturn"
        minSdk = 26 // Sesuai SRS: Android 8.0 ke atas
        targetSdk = 35
        versionCode = 2
        versionName = "0.1" // Sesuai versi draft SRS

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Sesuai SRS: Mencegah data ter-backup ke Google Cloud
        manifestPlaceholders["allowBackup"] = "false"
    }

    buildTypes {
        release {
            isMinifyEnabled = true // Disarankan aktif untuk rilis (Obfuscation)
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // UI Compose & Material 3
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)

    // Security (EncryptedSharedPreferences untuk Login)
    implementation(libs.androidx.security.crypto)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-extended:1.6.1")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
}