package com.apialerts

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstantsTest {

    @Test
    fun `test base url`() {
        assertEquals("https://api.apialerts.com", BASE_URL)
    }

    @Test
    fun `test integration name`() {
        assertEquals("kotlin", INTEGRATION_NAME)
    }
}