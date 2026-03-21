package com.apialerts.client.routes

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.contract.EventResponse
import com.apialerts.client.util.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal interface EventRoutes {
    suspend fun send(
        apiKey: String,
        payload: EventRequest,
        integration: String,
        version: String,
        baseUrl: String,
    ): EventResponse
}

internal class EventRoutesImpl(
    private val httpClient: HttpClient = createHttpClient()
) : EventRoutes {

    override suspend fun send(
        apiKey: String,
        payload: EventRequest,
        integration: String,
        version: String,
        baseUrl: String,
    ): EventResponse {
        return httpClient.post("$baseUrl/event") {
            header("Authorization", "Bearer $apiKey")
            header("Content-Type", "application/json")
            header("X-Integration", integration)
            header("X-Version", version)
            setBody(payload)
        }.body()
    }
}
