package com.apialerts.network.contract

import com.apialerts.network.util.json
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class EventResponseTest {

    @Test
    fun `test EventResponse serialization`() {
        val eventResponse = EventResponse(
            project = "TestProject",
            remainingQuota = 100,
            errors = listOf("Error1", "Error2")
        )
        val jsonString = json.encodeToString(eventResponse)
        assertEquals("""{"project":"TestProject","remainingQuota":100,"errors":["Error1","Error2"]}""", jsonString)
    }

    @Test
    fun `test EventResponse deserialization`() {
        val jsonString = """{"project":"TestProject","remainingQuota":100,"errors":["Error1","Error2"]}"""
        val eventResponse = json.decodeFromString<EventResponse>(jsonString)
        assertEquals("TestProject", eventResponse.project)
        assertEquals(100, eventResponse.remainingQuota)
        assertEquals(listOf("Error1", "Error2"), eventResponse.errors)
    }
}