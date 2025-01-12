package com.apialerts.network.contract

import com.apialerts.network.util.json
import kotlinx.serialization.encodeToString
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
        val jsonString = json.encodeToString(response)
        assertEquals("""{"workspace":"My Workspace","channel":"My Channel","remainingQuota":100,"errors":["Error1","Error2"]}""", jsonString)
    }

    @Test
    fun `test EventResponse deserialization`() {
        val jsonString = """{"workspace":"My Workspace","channel":"My Channel","remainingQuota":100,"errors":["Error1","Error2"]}"""
        val response = json.decodeFromString<EventResponse>(jsonString)
        assertEquals("My Workspace", response.workspace)
        assertEquals("My Channel", response.channel)
        assertEquals(100, response.remainingQuota)
        assertEquals(listOf("Error1", "Error2"), response.errors)
    }
}