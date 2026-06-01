package com.apialerts.client.util

import com.apialerts.client.TIMEOUT_MS
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
        requestTimeoutMillis = TIMEOUT_MS
        connectTimeoutMillis = TIMEOUT_MS
        socketTimeoutMillis = TIMEOUT_MS
    }
    expectSuccess = true
}

internal val networkJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}
