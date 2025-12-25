plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":library"))
    implementation(libs.coroutines.core)
}

application {
    mainClass.set("com.apialerts.sample.ApplicationKt")
}

