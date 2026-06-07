plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinMultiplatformLibrary) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.vanniktechMavenPublish) apply false
    alias(libs.plugins.kotlinter) apply false
}

subprojects {
    apply(plugin = "org.jmailen.kotlinter")

    tasks.withType<org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask>().configureEach {
        exclude { it.file.path.contains("build") }
    }
}
