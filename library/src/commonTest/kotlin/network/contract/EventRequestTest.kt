package com.apialerts.network.contract

import kotlin.test.Test
import kotlin.test.assertNotNull

class EventRequestTest {

    @Test
    fun `test constructor defaults`() {
        val eventNulls = EventRequest(
            channel = null,
            message = "message",
            link = null,
            tags = null
        )
        assertNotNull(eventNulls)
    }

    @Test
    fun `test constructor types`() {
        val eventTypes = EventRequest(
            channel = "default",
            message = "message",
            link = "https://apialerts.com",
            tags = listOf("tag 1", "tag 2")
        )
        assertNotNull(eventTypes)
    }
}