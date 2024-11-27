package com.apialerts.network.util

import com.apialerts.BASE_URL
import com.apialerts.INTEGRATION_NAME
import com.apialerts.VERSION
import com.apialerts.network.ResourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URI

internal suspend fun <T> request(
    path: String,
    method: String,
    apiKey: String,
    payload: String,
    serializer: KSerializer<T>
): ResourceResult<T> {
    val uri = URI("$BASE_URL/$path")
    val url = uri.toURL()
    val connection = withContext(Dispatchers.IO) {
        url.openConnection()
    } as HttpURLConnection
    connection.requestMethod = method
    connection.setRequestProperty("Authorization", "Bearer $apiKey")
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("X-Integration", INTEGRATION_NAME)
    connection.setRequestProperty("X-Device-Name", VERSION)
    connection.doOutput = true

    connection.outputStream.use { it.write(payload.toByteArray()) }

    val responseCode = connection.responseCode
    return when (responseCode) {
        HttpURLConnection.HTTP_OK -> {
            val response = connection.inputStream.bufferedReader().use(BufferedReader::readText)
            val eventResponse = json.decodeFromString(serializer, response)
            ResourceResult.Success(eventResponse)
        }
        else -> {
            val errorObject = errorHandler(connection)
            ResourceResult.Error(errorObject)
        }
    }
}
