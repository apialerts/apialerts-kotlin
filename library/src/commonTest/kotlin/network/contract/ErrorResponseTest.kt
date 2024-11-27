package com.apialerts.network.contract

import com.apialerts.network.util.json
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorResponseTest {

    @Test
    fun `test ErrorResponse serialization`() {
        val errorResponse = ErrorResponse(message = "Test Error")
        val string = json.encodeToString(errorResponse)
        assertEquals("""{"message":"Test Error"}""", string)
    }

    @Test
    fun `test ErrorResponse deserialization`() {
        val string = """{"message":"Test Error"}"""
        val errorResponse = json.decodeFromString<ErrorResponse>(string)
        assertEquals("Test Error", errorResponse.message)
    }
}