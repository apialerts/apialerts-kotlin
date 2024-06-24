package com.apialerts.client.network

import com.apialerts.client.BASE_URL
import com.apialerts.client.contract.EventRequest
import com.apialerts.client.contract.EventResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class Network(
    private val client: HttpClient
) {
    suspend fun send(apiKey: String, payload: EventRequest): ResourceResult<EventResponse> {
        return try {
            val response = client.post("$BASE_URL/event") {
                headers {
                    this["Authorization"] = "Bearer $apiKey"
                }
                contentType(ContentType.Application.Json)
                setBody(payload)
            }.body<EventResponse>()
            ResourceResult.Success(response)
        } catch (e: Exception) {
            ResourceResult.Error(e.asNetworkError())
        }
    }
}

