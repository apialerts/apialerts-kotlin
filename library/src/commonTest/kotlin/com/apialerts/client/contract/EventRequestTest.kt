package com.apialerts.client.contract

import kotlin.test.Test
import kotlin.test.assertNotNull

class EventRequestTest {

    @Test
    fun `test constructor minimal`() {
        val event = EventRequest(message = "message")
        assertNotNull(event)
    }

    @Test
    fun `test constructor all fields`() {
        val event = EventRequest(
            message = "message",
            channel = "default",
            event = "ci.deploy",
            title = "Deployed",
            tags = listOf("tag1", "tag2"),
            link = "https://apialerts.com",
        )
        assertNotNull(event)
    }
}