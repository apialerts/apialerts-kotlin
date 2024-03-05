plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = "com.apialerts.kotlin.client"
version = "0.0.1"

publishing {
    repositories {
        maven {
            //...
        }
    }
}

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
    namespace = "com.apialerts.kotlin.client"
    compileSdk = 34
    defaultConfig {
        namespace = "io.github.aakira.napier"
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
        targetCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
    }
}

