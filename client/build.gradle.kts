plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = "com.apialerts.client"
version = "0.0.1"

val GITHUB_USER: String by project
val GITHUB_TOKEN: String by project

kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget {
        publishAllLibraryVariants()
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutinesCore)
            implementation(libs.kotlinSerialization)
            implementation(libs.ktorClientCore)
            implementation(libs.ktorClientJson)
            implementation(libs.ktorClientNegotiation)
            implementation(libs.ktorClientSerialization)
            implementation(libs.ktorJson)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(libs.ktorClientJava)
        }
        androidMain.dependencies {
            implementation(libs.ktorClientAndroid)
        }
    }
}

android {
    namespace = "com.apialerts.client"
    compileSdk = libs.versions.androidTargetSdk.get().toInt()

    defaultConfig {
        namespace = "com.apialerts.client"
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
        targetCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
    }
}

