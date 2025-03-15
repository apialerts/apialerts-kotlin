import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.vanniktechMavenPublish)
}

group = "com.apialerts"
version = "1.0.1"

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutinesCore)
            implementation(libs.kotlinSerialization)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.mockito)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "client", version.toString())

    pom {
        name = "API Alerts"
        description = "Kotlin SDK for the API Alerts platform"
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
