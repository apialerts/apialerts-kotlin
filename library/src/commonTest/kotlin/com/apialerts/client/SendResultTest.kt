package com.apialerts.client

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SendResultTest {

    @Test
    fun `success result has success true and no error`() {
        val result = SendResult(
            success = true,
            workspace = "Acme Corp",
            channel = "general",
            warnings = emptyList(),
        )
        assertTrue(result.success)
        assertEquals("Acme Corp", result.workspace)
        assertEquals("general", result.channel)
        assertTrue(result.warnings.isEmpty())
        assertNull(result.error)
    }

    @Test
    fun `failure result has success false and error message`() {
        val result = SendResult(success = false, error = "client not configured")
        assertFalse(result.success)
        assertEquals("client not configured", result.error)
        assertNull(result.workspace)
        assertNull(result.channel)
        assertTrue(result.warnings.isEmpty())
    }

    @Test
    fun `success result with warnings`() {
        val result = SendResult(
            success = true,
            workspace = "My Workspace",
            channel = "alerts",
            warnings = listOf("unknown field: foo", "tag limit reached"),
        )
        assertTrue(result.success)
        assertEquals(2, result.warnings.size)
        assertEquals("unknown field: foo", result.warnings[0])
        assertNull(result.error)
    }

    @Test
    fun `default values are applied`() {
        val result = SendResult(success = true)
        assertTrue(result.warnings.isEmpty())
        assertNull(result.workspace)
        assertNull(result.channel)
        assertNull(result.error)
    }
}
