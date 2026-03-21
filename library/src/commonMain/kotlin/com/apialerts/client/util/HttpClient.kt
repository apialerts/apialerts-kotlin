package com.apialerts.client.util

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(networkJson)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30000L
        connectTimeoutMillis = 30000L
        socketTimeoutMillis = 30000L
    }
    expectSuccess = true
}

internal val networkJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}
