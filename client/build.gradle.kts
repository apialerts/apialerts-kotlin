plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = "com.apialerts.kotlin.client"
version = "0.0.1"

val GITHUB_USER: String by project
val GITHUB_TOKEN: String by project

publishing {
    repositories {
        maven {
            setUrl("https://maven.pkg.github.com/apialerts/apialerts-kotlin")
            credentials {
                username = GITHUB_USER
                password = GITHUB_TOKEN
            }
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
    compileSdk = libs.versions.androidTargetSdk.get().toInt()

    defaultConfig {
        namespace = "com.apialerts.kotlin.client"
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
        targetCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
    }
}

