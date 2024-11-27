package com.apialerts.network.util

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ErrorHandlerTest {

    @Test
    fun `test valid error response`() {
        val connection = mock(HttpURLConnection::class.java)
        val errorJson = """{"message":"Test Error"}"""
        val inputStream: InputStream = ByteArrayInputStream(errorJson.toByteArray())

        `when`(connection.responseCode).thenReturn(400)
        `when`(connection.inputStream).thenReturn(inputStream)

        val result = errorHandler(connection)
        assertEquals("Test Error", result.message)
    }

    @Test
    fun `test invalid error response`() {
        val connection = mock(HttpURLConnection::class.java)
        val invalidJson = """{"invalid":"response"}"""
        val inputStream: InputStream = ByteArrayInputStream(invalidJson.toByteArray())

        `when`(connection.responseCode).thenReturn(400)
        `when`(connection.inputStream).thenReturn(inputStream)

        val result = errorHandler(connection)
        assertNull(result.message)
    }

    @Test
    fun `test exception during response code retrieval`() {
        val connection = mock(HttpURLConnection::class.java)

        `when`(connection.responseCode).thenThrow(RuntimeException::class.java)
        `when`(connection.inputStream).thenThrow(RuntimeException::class.java)

        val result = errorHandler(connection)
        assertEquals("Server responded with null", result.message)
    }

    @Test
    fun `test string error response`() {
        val connection = mock(HttpURLConnection::class.java)
        val invalidJson = "A test error response"
        val inputStream: InputStream = ByteArrayInputStream(invalidJson.toByteArray())

        `when`(connection.responseCode).thenReturn(400)
        `when`(connection.inputStream).thenReturn(inputStream)

        val result = errorHandler(connection)
        assertEquals("Server responded with 400", result.message)
    }

    @Test
    fun `test connection timeout`() {
        val connection = mock(HttpURLConnection::class.java)

        `when`(connection.responseCode).thenThrow(IOException("Connection timed out"))

        val result = errorHandler(connection)
        assertEquals("Server responded with null", result.message)
    }

    @Test
    fun `test connection error`() {
        val connection = mock(HttpURLConnection::class.java)

        `when`(connection.responseCode).thenThrow(IOException("Connection error"))

        val result = errorHandler(connection)
        assertEquals("Server responded with null", result.message)
    }
}
