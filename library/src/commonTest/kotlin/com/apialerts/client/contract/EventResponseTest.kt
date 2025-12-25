package com.apialerts.client.contract

import com.apialerts.client.contract.EventResponse
import com.apialerts.client.util.networkJson
import kotlin.test.Test
import kotlin.test.assertEquals

class EventResponseTest {

    @Test
    fun `test EventResponse serialization`() {
        val response = EventResponse(
            workspace = "My Workspace",
            channel = "My Channel",
            remainingQuota = 100,
            errors = listOf("Error1", "Error2")
        )
        val jsonString = networkJson.encodeToString(response)
        assertEquals("""{"workspace":"My Workspace","channel":"My Channel","remainingQuota":100,"errors":["Error1","Error2"]}""", jsonString)
    }

    @Test
    fun `test EventResponse deserialization`() {
        val jsonString = """{"workspace":"My Workspace","channel":"My Channel","remainingQuota":100,"errors":["Error1","Error2"]}"""
        val response = networkJson.decodeFromString<EventResponse>(jsonString)
        assertEquals("My Workspace", response.workspace)
        assertEquals("My Channel", response.channel)
        assertEquals(100, response.remainingQuota)
        assertEquals(listOf("Error1", "Error2"), response.errors)
    }
}