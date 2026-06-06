package com.apialerts.sample

import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event
import kotlin.system.exitProcess
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Used in the GitHub Action workflow to send an event on build success and publish
 * Accepts one of --build, --release, --publish, or --integration-tests arguments
 */
fun main(args: Array<String>) {
    runBlocking {
        val apiKey = System.getenv("APIALERTS_API_KEY") ?: ""
        if (apiKey.isEmpty()) {
            System.err.println("Error: APIALERTS_API_KEY environment variable is not set")
            exitProcess(1)
        }
        ApiAlerts.configure(apiKey = apiKey, debug = true)

        val link = "https://github.com/apialerts/apialerts-kotlin/actions"
        val channelIdx = args.indexOf("--channel")
        val channel = if (channelIdx >= 0) args.getOrNull(channelIdx + 1) ?: "testing" else "testing"

        when {
            args.any { it == "--build" } -> {
                ApiAlerts.sendAsync {
                    message = "Kotlin - PR build success"
                    this.channel = "developer"
                    event = "ci.sdk.build.kotlin"
                    title = "Build Passed"
                    tags = listOf("CI/CD", "Kotlin", "Build")
                    this.link = link
                }.onSuccess {
                    println("✓ Sent to ${it.workspace} (${it.channel})")
                }.onFailure {
                    System.err.println("x Failed: ${it.message}")
                    exitProcess(1)
                }
            }
            args.any { it == "--release" } -> {
                ApiAlerts.sendAsync {
                    message = "Kotlin - Build for publish success"
                    this.channel = "developer"
                    event = "ci.sdk.release.kotlin"
                    title = "Release Build Passed"
                    tags = listOf("CI/CD", "Kotlin", "Build")
                    this.link = link
                }.onSuccess {
                    println("✓ Sent to ${it.workspace} (${it.channel})")
                }.onFailure {
                    System.err.println("x Failed: ${it.message}")
                    exitProcess(1)
                }
            }
            args.any { it == "--publish" } -> {
                ApiAlerts.sendAsync {
                    message = "Kotlin - Maven publish success"
                    this.channel = "releases"
                    event = "ci.sdk.publish.kotlin"
                    title = "Published"
                    tags = listOf("CI/CD", "Kotlin", "Deploy")
                    this.link = link
                }.onSuccess {
                    println("✓ Sent to ${it.workspace} (${it.channel})")
                }.onFailure {
                    System.err.println("x Failed: ${it.message}")
                    exitProcess(1)
                }
            }
            args.any { it == "--integration-tests" } -> {
                // Minimal — message only
                ApiAlerts.sendAsync(Event(message = "Kotlin SDK - minimal", channel = channel))
                    .onSuccess { println("✓ Sent to ${it.workspace} (${it.channel})") }
                    .onFailure { System.err.println("x Error (minimal): ${it.message}"); exitProcess(1) }

                // Full — all fields
                ApiAlerts.sendAsync(Event(
                    message = "Kotlin SDK - full",
                    channel = channel,
                    event = "sdk.test",
                    title = "Integration Test",
                    tags = listOf("CI/CD", "Kotlin"),
                    link = link,
                    data = buildJsonObject { put("version", "1.1.0") },
                )).onSuccess {
                    println("✓ Sent to ${it.workspace} (${it.channel})")
                    it.warnings.forEach { w -> println("! Warning: $w") }
                }.onFailure {
                    System.err.println("x Error (full): ${it.message}")
                    exitProcess(1)
                }
            }
            else -> {
                System.err.println("Error: pass --build, --release, --publish, or --integration-tests")
                exitProcess(1)
            }
        }
    }
}
