package com.apialerts.client.routes

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.contract.EventResponse
import com.apialerts.client.util.ResourceResult
import com.apialerts.client.util.asNetworkError
import com.apialerts.client.util.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal interface EventRoutes {
    suspend fun send(apiKey: String, payload: EventRequest): ResourceResult<EventResponse>
}

internal class EventRoutesImpl(
    private val httpClient: HttpClient = createHttpClient()
): EventRoutes {

    override suspend fun send(apiKey: String, payload: EventRequest): ResourceResult<EventResponse> {
        val route = "/event"
        return try {
            val response = httpClient.post(route) {
                setBody(payload)
                header("Authorization", "Bearer $apiKey")
            }.body<EventResponse>()
            ResourceResult.Success(response)
        } catch (e: Exception) {
            val error = e.asNetworkError()
            ResourceResult.Error(error)
        }
    }
}
