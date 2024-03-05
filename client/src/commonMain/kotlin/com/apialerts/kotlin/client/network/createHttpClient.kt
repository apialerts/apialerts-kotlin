package com.apialerts.kotlin.client.network

import com.apialerts.kotlin.client.INTEGRATION_NAME
import com.apialerts.kotlin.client.VERSION
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(
    engine: HttpClientEngine
) = HttpClient(engine) {
    install(ContentNegotiation) {
        json(Json {
            isLenient = false
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 50000L
        connectTimeoutMillis = 20000L
    }
    defaultRequest {
        header("X-Integration", INTEGRATION_NAME)
        header("X-Device-Name", VERSION)
    }
    this.expectSuccess = true
}
