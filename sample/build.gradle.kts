plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":library"))
    implementation(libs.coroutines.core)
    implementation(libs.kotlin.serialization)
}

application {
    mainClass.set("com.apialerts.sample.ApplicationKt")
}

