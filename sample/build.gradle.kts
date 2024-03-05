plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(project(":client"))
    implementation(libs.coroutinesCore)
}

java {
    sourceCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
    targetCompatibility = JavaVersion.valueOf("VERSION_" + libs.versions.javaSdk.get())
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = libs.versions.javaSdk.get()
}

application {
    mainClass.set("com.apialerts.sample.ApplicationKt")
}

