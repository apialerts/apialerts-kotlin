package com.apialerts.sample

import com.apialerts.ApiAlerts

// Initial configuration
fun sampleConfigure() {
    ApiAlerts.configure(
        apiKey = "your-api-key",
        debug = true
    )
}

// Simple fire and forget samples
// Preferred for most use cases
fun sampleSimple() {

    // 1. DSL style
    ApiAlerts.send {
        apiKey = "your-api-key"
        channel = "testing"
        message = "Full integration of apialerts-kotlin"
        tags = listOf("integration", "kotlin")
        link = "https://github.com/apialerts/apialerts-kotlin"
    }

    // 2. Simple style
    ApiAlerts.send(
        message = "Minimal integration of apialerts-kotlin",
    )
}

// Async sample where the send function will wait for a response
// Can be useful in serverless environments where code execution is killed before the event is sent
suspend fun sampleAsync() {

    // 1. DSL style - async
    ApiAlerts.sendAsync{
        message = "Full integration of apialerts-kotlin"
    }

    // 2. Simple style - async
    ApiAlerts.sendAsync(
        message = "Minimal integration of apialerts-kotlin",
    )
}