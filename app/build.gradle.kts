plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projekakhir_andrewijaya"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.projekakhir_andrewijaya"
        minSdk = 24
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

    // PERBAIKAN: Menyesuaikan versi Java ke standar umum untuk kompatibilitas.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // PENAMBAHAN: Mengaktifkan fitur ViewBinding untuk akses komponen UI yang lebih baik.
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // --- Dependensi Inti & UI (sudah ada) ---
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // --- PERBAIKAN: Dependensi untuk Open Street Maps (osmdroid) (using version catalog) ---
    implementation(libs.osmdroid.android)

    // --- Dependensi untuk Testing (sudah ada) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}