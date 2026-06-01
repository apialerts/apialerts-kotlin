import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.vanniktechMavenPublish)
}

group = "com.apialerts"
version = libs.versions.libraryVersion.get()

kotlin {
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "com.apialerts.client"
        compileSdk = libs.versions.androidTargetSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()

        @Suppress("UnstableApiUsage")
        optimization {
            consumerKeepRules.publish = true
            consumerKeepRules.files.add(File("consumer-rules.pro"))
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.javaSdk.get()))
        }
    }

    jvm()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.javaSdk.get()))
    }

    js {
        browser()
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }

    // Apple - ARM64 only (Apple Silicon era)
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
    watchosArm64()
    watchosSimulatorArm64()
    tvosArm64()
    tvosSimulatorArm64()

    // Linux native
    linuxX64()
    linuxArm64()

    // Windows native
    mingwX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.kotlin.serialization)
            implementation(libs.kermit)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        linuxMain.dependencies {
            implementation(libs.ktor.client.curl)
        }
        mingwMain.dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(false)

    signAllPublications()

    coordinates(group.toString(), "client", version.toString())

    pom {
        name = "API Alerts"
        description = "Kotlin Multiplatform SDK for the API Alerts platform"
        inceptionYear = "2024"
        url = "https://github.com/apialerts/apialerts-kotlin"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "apialerts"
                name = "API Alerts"
                url = "https://github.com/apialerts"
            }
        }
        scm {
            url = "https://github.com/apialerts/apialerts-kotlin"
            connection = "scm:git:git://github.com/apialerts/apialerts-kotlin.git"
            developerConnection = "scm:git:ssh://git@github.com/apialerts/apialerts-kotlin.git"
        }
    }
}
