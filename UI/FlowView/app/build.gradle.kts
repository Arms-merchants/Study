plugins {
    id ("com.android.application")
    id ("kotlin-android")
}

android {
    compileSdk =31

    defaultConfig {
        applicationId = "com.arms.flowview"
        minSdk = 21
        targetSdk =  30
        versionCode =  1
        versionName =  "1.0"
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

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.1")
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    //ktx
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.collection:collection-ktx:1.2.0-beta01")
    implementation ("androidx.fragment:fragment-ktx:1.3.6")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation ("androidx.navigation:navigation-runtime-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")

    //recyclerviewadapter
    implementation ("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.7")

}