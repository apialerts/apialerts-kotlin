package com.apialerts.client.util

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorHandlingTest {

    @Test
    fun `asNetworkError returns Connect Timeout for ConnectTimeoutException`() = runTest {
        val exception = io.ktor.client.network.sockets.ConnectTimeoutException("http://localhost")
        val error = exception.asNetworkError()
        assertEquals("Connect Timeout", error.message)
    }

    @Test
    fun `asNetworkError returns Request Timeout for HttpRequestTimeoutException`() = runTest {
        val exception = io.ktor.client.plugins.HttpRequestTimeoutException("http://localhost", 1000)
        val error = exception.asNetworkError()
        assertEquals("Request Timeout", error.message)
    }

    @Test
    fun `asNetworkError returns Network change detected for connection abort`() = runTest {
        val exception = Exception("Software caused connection abort")
        val error = exception.asNetworkError()
        assertEquals("Network change detected", error.message)
    }

    @Test
    fun `asNetworkError returns Cannot parse response for lenient error`() = runTest {
        val exception = Exception("Use JsonReader.setLenient")
        val error = exception.asNetworkError()
        assertEquals("Cannot parse response", error.message)
    }

    @Test
    fun `asNetworkError returns No internet access for failed to connect`() = runTest {
        val exception = Exception("Failed to connect to localhost/127.0.0.1:8080")
        val error = exception.asNetworkError()
        assertEquals("No internet access", error.message)
    }

    @Test
    fun `asNetworkError returns Unknown Error for generic exception`() = runTest {
        val exception = Exception("Some random error")
        val error = exception.asNetworkError()
        assertEquals("Unknown Error. Please try again later.", error.message)
    }
}
