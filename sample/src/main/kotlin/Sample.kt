package com.apialerts.sample

import com.apialerts.client.ApiAlerts
import com.apialerts.client.Event

// Initial configuration
fun sampleConfigure() {
    ApiAlerts.configure(
        apiKey = "your-api-key",
        debug = true
    )
}

// Fire-and-forget — preferred for most use cases
fun sampleSimple() {

    // Event object style
    ApiAlerts.send(Event(message = "Minimal send"))

    // DSL builder style
    ApiAlerts.send {
        message = "Full send"
        channel = "developer"
        event = "ci.build"
        title = "Build complete"
        tags = listOf("CI/CD", "Kotlin")
        link = "https://github.com/apialerts/apialerts-kotlin"
    }
}

// Async — waits for the response; useful in serverless where the process exits immediately
suspend fun sampleAsync() {

    // Event object style
    val result = ApiAlerts.sendAsync(Event(message = "Minimal async send"))
    if (result.success) {
        println("Sent to ${result.workspace} (${result.channel})")
    } else {
        println("Error: ${result.error}")
    }

    // DSL builder style
    ApiAlerts.sendAsync {
        message = "Full async send"
        channel = "developer"
    }
}
