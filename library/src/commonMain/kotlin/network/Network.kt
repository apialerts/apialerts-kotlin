package com.apialerts.network

import com.apialerts.network.contract.ErrorResponse
import com.apialerts.network.contract.EventRequest
import com.apialerts.network.contract.EventResponse
import com.apialerts.network.util.request
import com.apialerts.network.util.serializeData

internal interface Network {
    suspend fun send(apiKey: String, payload: EventRequest): ResourceResult<EventResponse>
}

internal class NetworkImpl: Network {

    override suspend fun send(apiKey: String, payload: EventRequest): ResourceResult<EventResponse> {
        return try {
            val json = serializeData(payload, EventRequest.serializer())
            return request("event", "POST", apiKey, json, EventResponse.serializer())
        } catch (e: Exception) {
            val error = ErrorResponse(e.message ?: "Unknown error")
            ResourceResult.Error(error)
        }
    }
}

