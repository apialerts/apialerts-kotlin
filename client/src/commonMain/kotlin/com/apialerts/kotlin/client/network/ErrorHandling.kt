package com.apialerts.kotlin.client.network

import com.apialerts.kotlin.client.contract.ErrorResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException

suspend fun Throwable.asNetworkError(): ErrorObject {
    return when (this) {
        is ClientRequestException -> {
            getClientError(this)
        }
        is Exception -> {
            ErrorObject(this.message ?: "Unknown Error")
        }
        else -> {
            ErrorObject("Unknown Error ${this.message}")
        }
    }
}

private suspend fun getClientError(exception: ClientRequestException): ErrorObject {
    try {
        val error = exception.response.body<ErrorResponse>()
        // try get standard error response
        val errorMessage = error.message
        if (errorMessage?.isNotBlank() == true) {
            return ErrorObject(errorMessage)
        }
        return ErrorObject("Something went wrong")
    } catch (e: NoTransformationFoundException) {
        return ErrorObject("Bad response")
    } catch (e: Exception) {
        return ErrorObject("Unknown Error")
    }
}
