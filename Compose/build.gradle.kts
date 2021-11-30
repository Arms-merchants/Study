// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Version.ANDROID_GRADLE_PLUGIN}")

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}")
    }
}

tasks.register("Delete", Delete::class) {
    delete(rootProject.buildDir)
}