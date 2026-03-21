package com.apialerts.sample

import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Used in the GitHub Action workflow to send an event on build success and publish
 * Accepts one of --build, --release, --publish arguments
 */
fun main(args: Array<String>) = runBlocking {

    ApiAlerts.configure(
        apiKey = System.getenv("APIALERTS_API_KEY") ?: throw IllegalArgumentException("'APIALERTS_API_KEY' environment variable not provided"),
        debug = true
    )

    val link = "https://github.com/apialerts/apialerts-kotlin/actions"

    when {
        // SDK CI notifications — called from build-release.yml / publish-maven.yml
        args.any { it == "--build" } -> {
            val result = ApiAlerts.sendAsync {
                message = "Kotlin - PR build success"
                channel = "developer"
                event = "ci.build"
                title = "Build Passed"
                tags = listOf("CI/CD", "Kotlin", "Build")
                this.link = link
            }
            if (result.success) {
                println("✓ Sent to ${result.workspace} (${result.channel})")
            } else {
                println("x Failed: ${result.error}")
            }
        }
        args.any { it == "--release" } -> {
            val result = ApiAlerts.sendAsync {
                message = "Kotlin - Build for publish success"
                channel = "developer"
                event = "ci.release"
                title = "Release Build Passed"
                tags = listOf("CI/CD", "Kotlin", "Build")
                this.link = link
            }
            if (result.success) {
                println("✓ Sent to ${result.workspace} (${result.channel})")
            } else {
                println("x Failed: ${result.error}")
            }
        }
        args.any { it == "--publish" } -> {
            val result = ApiAlerts.sendAsync {
                message = "Kotlin - Maven publish success"
                channel = "releases"
                event = "ci.publish"
                title = "Published"
                tags = listOf("CI/CD", "Kotlin", "Deploy")
                this.link = link
            }
            if (result.success) {
                println("✓ Sent to ${result.workspace} (${result.channel})")
            } else {
                println("x Failed: ${result.error}")
            }
        }
        // Integration test — called from apialerts-integration-tests with no args
        else -> {
            // Minimal — message only
            val r1 = ApiAlerts.sendAsync(Event(message = "Kotlin SDK - minimal"))
            if (r1.success) {
                println("✓ sent to ${r1.workspace} (${r1.channel})")
            } else {
                println("x Failed: ${r1.error}")
            }

            // Full — all fields
            val r2 = ApiAlerts.sendAsync(Event(
                message = "Kotlin SDK - full",
                channel = "developer",
                event = "sdk.test",
                title = "Integration Test",
                tags = listOf("CI/CD", "Kotlin"),
                link = link,
                data = buildJsonObject { put("version", "2.0.0") },
            ))
            if (r2.success) {
                println("✓ sent to ${r2.workspace} (${r2.channel})")
            } else {
                println("x Failed: ${r2.error}")
            }
        }
    }
}
