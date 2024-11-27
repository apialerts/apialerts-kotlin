package com.apialerts.sample

import com.apialerts.ApiAlerts
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    ApiAlerts.configure(
        apiKey = "your-api-key",
        logging = true
    )

    // DSL style
    ApiAlerts.send {
        apiKey = "your-api-key"
        channel = "testing"
        message = "Full integration of apialerts-kotlin"
        tags = listOf("integration", "kotlin")
        link = "https://github.com/apialerts/apialerts-kotlin"
    }

    // Simple style
    ApiAlerts.send(
        message = "Minimal integration of apialerts-kotlin",
    )

    // delay to ensure the messages are sent
    delay(10000)
}