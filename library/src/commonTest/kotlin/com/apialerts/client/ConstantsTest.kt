package com.apialerts.client

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pins the literal values of SDK constants. Catches accidental changes to
 * the integration name, version shape, base URL, or timeout - matches the
 * canonical pattern documented in SDK Design.
 */
class ConstantsTest {

    @Test
    fun `integration name is kotlin`() {
        assertEquals("kotlin", INTEGRATION_NAME)
    }

    @Test
    fun `base url is apialerts event endpoint`() {
        assertEquals("https://api.apialerts.com/event", BASE_URL)
    }

    @Test
    fun `timeout is 30 seconds per spec`() {
        assertEquals(30_000L, TIMEOUT_MS)
    }

    @Test
    fun `version is one x semver`() {
        // Major version pin: bumping to v2.x requires updating this test
        // deliberately. Catches accidental major bumps.
        val regex = Regex("""^1\.\d+\.\d+(?:[-+][\w.]+)?$""")
        assertTrue(
            regex.matches(VERSION),
            "version $VERSION does not match 1.x semver",
        )
    }
}
