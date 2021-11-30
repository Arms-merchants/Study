plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.arm.composechat"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
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
    composeOptions {
        kotlinCompilerExtensionVersion = Version.COMPOSE
    }

    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation("androidx.compose.ui:ui-tooling-preview:+")
    testImplementation(Libs.JUNIT)
    androidTestImplementation(Libs.TEST_EXT)
    androidTestImplementation(Libs.ESPRESSO)

    implementation(Libs.APPCOMPAT)
    implementation(Libs.MATERIAL)
    implementation(Libs.KOTLINX_COROUTINES)
    implementation(Libs.COMPOSE_COIL)
    implementation(Libs.ACTIVITY_COMPOSE)
    implementation(Libs.VIEWMODEL_COMPOSE)
    implementation(Libs.COMPOSE_CONSTRAINTLAYOUT)
    implementation(Libs.COMPOSE_NAVIGATION)
    implementation(Libs.COMPOSE_MATERIAL)
    implementation(Libs.COMPOSE_MATERIAL_ICONS)
    implementation(Libs.COMPOSE_LIVEDATA)
    implementation(Libs.COMPOSE_UI)
    implementation(Libs.COMPOSE_UI_TOOLING)
    androidTestImplementation(Libs.COMPOSE_UI_TEST_JUNIT4)
    implementation(Libs.ACCOMPANIST_INSETS)

    implementation(Libs.ACCOMPANIST_SYSTEMUICONTROLLER)
    implementation(Libs.ACCOMPANIST_NAVIGATION_ANIMATION)

    implementation(project(":base"))
    implementation(project(":proxy"))

}