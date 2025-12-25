package com.apialerts.util

import com.apialerts.contract.ErrorResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException

internal suspend fun Throwable.asNetworkError(): ErrorObject {
    return when {
        this is ClientRequestException -> {
            getClientError(this)
        }
        this is ServerResponseException -> {
            getServerError(this)
        }
        this is ConnectTimeoutException -> {
            ErrorObject("Connect Timeout")
        }
        this is HttpRequestTimeoutException -> {
            ErrorObject("Request Timeout")
        }
        this.message == "Software caused connection abort" -> {
            ErrorObject("Network change detected")
        }
        this.message == "Use JsonReader.setLenient" -> {
            ErrorObject("Cannot parse response")
        }
        this.message?.startsWith("Failed to connect to") == true -> {
            ErrorObject("No internet access")
        }
        this is Exception -> {
            ErrorObject("Unknown Error. Please try again later.")
        }
        else -> {
            ErrorObject("Unknown Error")
        }
    }
}

private suspend fun getClientError(exception: ClientRequestException): ErrorObject {
    try {
        val error: ErrorResponse = exception.response.body<ErrorResponse>()
        // try get standard error response
        val errorMessage = error.message
        if (errorMessage?.isNotBlank() == true) {
            return ErrorObject(errorMessage)
        }
        // build error based on status
        val message = statusCodeToError(exception.response.status.value)
        return ErrorObject(message)
    } catch (e: NoTransformationFoundException) {
        return ErrorObject("Bad response")
    } catch (e: Exception) {
        return ErrorObject("Unknown Error?")
    }
}

private suspend fun getServerError(exception: ServerResponseException): ErrorObject {
    try {
        val error: ErrorResponse = exception.response.body<ErrorResponse>()
        // try get standard error response
        val errorMessage = error.message
        if (errorMessage?.isNotBlank() == true) {
            return ErrorObject(errorMessage)
        }
        // build error based on status
        val message = statusCodeToError(exception.response.status.value)
        return ErrorObject(message)
    } catch (e: NoTransformationFoundException) {
        return ErrorObject("Bad response")
    } catch (e: Exception) {
        return ErrorObject("Unknown Error?")
    }
}

private fun statusCodeToError(code: Int): String {
    return when (code) {
        204 -> "No content."
        401, 403 -> "Unauthorised"
        408, 504 -> "Timeout."
        503 -> "Maintenance"
        in 300..399 -> "API Currently Unavailable."
        in 400..499 -> "Unknown Error."
        in 500..599 -> "Server Error."
        else -> "Unknown Error"
    }
}
