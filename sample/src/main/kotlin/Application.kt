package com.apialerts.sample

import com.apialerts.ApiAlerts
import kotlinx.coroutines.runBlocking

/**
 * Used in the GitHub Action workflow to send an event on build success and publish
 * Accepts one of --build, --release, --publish arguments
 */
fun main(args: Array<String>) = runBlocking {

    ApiAlerts.configure(
        apiKey = System.getenv("APIALERTS_API_KEY") ?: throw IllegalArgumentException("'APIALERTS_API_KEY' environment variable not provided'"),
        debug = true
    )

    val eventChannel = "developer"
    var eventMessage = "apialerts-kotlin"
    var eventTags: List<String>? = null
    val eventLink = "https://github.com/apialerts/apialerts-kotlin/actions"

    when {
        args.any { it == "--build" } -> {
            eventMessage = "Kotlin - PR build success"
            eventTags = listOf("CI/CD", "Kotlin", "Build")
        }
        args.any { it == "--release" } -> {
            eventMessage = "Kotlin - Build for publish success"
            eventTags = listOf("CI/CD", "Kotlin", "Build")
        }
        args.any { it == "--publish" } -> {
            eventMessage = "Kotlin - Maven publish success"
            eventTags = listOf("CI/CD", "Kotlin", "Deploy")
        }
    }

    ApiAlerts.sendAsync {
        channel = eventChannel
        message = eventMessage
        tags = eventTags
        link = eventLink
    }
}
