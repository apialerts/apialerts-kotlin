package com.apialerts.client.contract

import com.apialerts.client.util.networkJson
import kotlin.test.Test
import kotlin.test.assertEquals

class EventResponseTest {

    @Test
    fun `test EventResponse serialization`() {
        val response = EventResponse(
            workspace = "My Workspace",
            channel = "My Channel",
            warnings = listOf("warn1", "warn2"),
        )
        val jsonString = networkJson.encodeToString(response)
        assertEquals("""{"workspace":"My Workspace","channel":"My Channel","warnings":["warn1","warn2"]}""", jsonString)
    }

    @Test
    fun `test EventResponse deserialization`() {
        val jsonString = """{"workspace":"My Workspace","channel":"My Channel","warnings":["warn1","warn2"]}"""
        val response = networkJson.decodeFromString<EventResponse>(jsonString)
        assertEquals("My Workspace", response.workspace)
        assertEquals("My Channel", response.channel)
        assertEquals(listOf("warn1", "warn2"), response.warnings)
    }

    @Test
    fun `test EventResponse ignores unknown keys`() {
        val jsonString = """{"workspace":"W","channel":"C","remainingQuota":100,"errors":["old"]}"""
        val response = networkJson.decodeFromString<EventResponse>(jsonString)
        assertEquals("W", response.workspace)
        assertEquals("C", response.channel)
    }
}