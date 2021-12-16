// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        //maven { url uri("/Users/heyueyang/lockMaven/repo") }
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    }
}

tasks.register("Delete", Delete::class) {
    delete(rootProject.buildDir)
}