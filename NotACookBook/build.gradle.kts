// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}
buildscript {
    repositories {
        google() // Make sure you have Google's Maven repository
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Add Google Services plugin
    }
}

allprojects {

}
