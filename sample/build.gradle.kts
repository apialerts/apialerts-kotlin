plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":library"))
    implementation(libs.coroutinesCore)
}

application {
    mainClass.set("com.apialerts.sample.ApplicationKt")
}

