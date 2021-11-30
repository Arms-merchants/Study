plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk =Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
        targetSdk = Version.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }

    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    testImplementation(Libs.JUNIT)
    androidTestImplementation(Libs.TEST_EXT)
    androidTestImplementation(Libs.ESPRESSO)
    implementation(Libs.KOTLINX_COROUTINES)
    implementation(Libs.T_IM)
    implementation(project(":base"))
}