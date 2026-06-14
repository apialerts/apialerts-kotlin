package com.apialerts.sample

import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event

// Initial configuration
fun sampleConfigure() {
    ApiAlerts.configure(
        apiKey = "your-api-key",
        debug = true,
    )
}

// Fire-and-forget - preferred for most use cases
fun sampleSimple() {
    // Event object style
    ApiAlerts.send(Event(message = "Minimal send"))

    // DSL builder style
    ApiAlerts.send {
        message = "Full send"
        channel = "developer"
        event = "ci.sdk.build.kotlin"
        title = "Build complete"
        tags = listOf("CI/CD", "Kotlin")
        link = "https://github.com/apialerts/apialerts-kotlin"
    }
}

// Async - waits for the response; useful in serverless where the process exits immediately
suspend fun sampleAsync() {
    // Event object style
    ApiAlerts.sendAsync(Event(message = "Minimal async send"))
        .onSuccess { println("Sent to ${it.workspace} (${it.channel})") }
        .onFailure { println("Error: ${it.message}") }

    // DSL builder style
    ApiAlerts.sendAsync {
        message = "Full async send"
        channel = "developer"
    }
}
