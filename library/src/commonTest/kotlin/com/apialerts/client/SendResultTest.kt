package com.apialerts.client

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SendResultTest {

    @Test
    fun `result has workspace and channel`() {
        val result = SendResult(workspace = "Acme Corp", channel = "general")
        assertEquals("Acme Corp", result.workspace)
        assertEquals("general", result.channel)
        assertTrue(result.warnings.isEmpty())
    }

    @Test
    fun `result with warnings`() {
        val result = SendResult(
            workspace = "My Workspace",
            channel = "alerts",
            warnings = listOf("unknown field: foo", "tag limit reached"),
        )
        assertEquals(2, result.warnings.size)
        assertEquals("unknown field: foo", result.warnings[0])
    }

    @Test
    fun `warnings defaults to empty list`() {
        val result = SendResult(workspace = "W", channel = "C")
        assertTrue(result.warnings.isEmpty())
    }
}
