package com.apialerts.client.contract

import com.apialerts.client.util.networkJson
import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorResponseTest {

    @Test
    fun `test ErrorResponse serialization`() {
        val errorResponse = ErrorResponse(message = "Test Error")
        val string = networkJson.encodeToString(errorResponse)
        assertEquals("""{"message":"Test Error"}""", string)
    }

    @Test
    fun `test ErrorResponse deserialization`() {
        val string = """{"message":"Test Error"}"""
        val errorResponse = networkJson.decodeFromString<ErrorResponse>(string)
        assertEquals("Test Error", errorResponse.message)
    }
}