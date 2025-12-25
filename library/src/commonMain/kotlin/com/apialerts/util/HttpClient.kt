package com.apialerts.util

import com.apialerts.BASE_URL
import com.apialerts.INTEGRATION_NAME
import com.apialerts.VERSION
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(networkJson)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 60000L
        connectTimeoutMillis = 60000L
        socketTimeoutMillis = 60000L
    }
    defaultRequest {
        url(BASE_URL)
        header("Content-Type", "application/json")
        header("X-Integration", INTEGRATION_NAME)
        header("X-Version", VERSION)
    }
    this.expectSuccess = true
}

internal val networkJson = Json {
    isLenient = false
    ignoreUnknownKeys = true
    coerceInputValues = true
    encodeDefaults = true
}
