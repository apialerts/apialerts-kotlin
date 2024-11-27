package com.apialerts.network.util

import com.apialerts.network.contract.ErrorResponse
import java.io.BufferedReader
import java.net.HttpURLConnection

internal fun errorHandler(connection: HttpURLConnection): ErrorResponse {
    val responseCode: Int? = try {
        connection.responseCode
    } catch (e: Exception) {
        null // could not parse response code
    }

    val response = try {
        connection.inputStream.bufferedReader().use(BufferedReader::readText)
    } catch (e: Exception) {
        return ErrorResponse("Server responded with $responseCode")
    }
    return try {
        json.decodeFromString(ErrorResponse.serializer(), response)
    } catch (e: Exception) {
        return ErrorResponse("Server responded with $responseCode")
    }
}