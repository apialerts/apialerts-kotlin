package com.apialerts.client

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.contract.EventResponse
import com.apialerts.client.routes.EventRoutes
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

// --- Fakes ---

private class SuccessRoutes(
    private val workspace: String = "Acme Corp",
    private val channel: String = "general",
    private val warnings: List<String>? = null,
) : EventRoutes {
    override suspend fun send(
        apiKey: String,
        payload: EventRequest,
        integration: String,
        version: String,
        baseUrl: String,
    ): EventResponse = EventResponse(workspace = workspace, channel = channel, warnings = warnings)
}

private class ThrowingRoutes(private val exception: Exception) : EventRoutes {
    override suspend fun send(
        apiKey: String,
        payload: EventRequest,
        integration: String,
        version: String,
        baseUrl: String,
    ): EventResponse = throw exception
}

// --- sendAsync validation tests ---

class ClientImplTest {

    @Test
    fun `sendAsync returns failure when not configured`() = runTest {
        val client = ClientImpl(api = SuccessRoutes())
        val result = client.sendAsync(Event(message = "hello"))
        assertTrue(result.isFailure)
        assertEquals("client not configured", result.exceptionOrNull()?.message)
    }

    @Test
    fun `sendAsync returns failure for blank message`() = runTest {
        val client = ClientImpl(api = SuccessRoutes())
        client.configure("test-key", false)
        val result = client.sendAsync(Event(message = "   "))
        assertTrue(result.isFailure)
        assertEquals("message is required", result.exceptionOrNull()?.message)
    }

    @Test
    fun `sendAsync returns success with workspace and channel`() = runTest {
        val client = ClientImpl(api = SuccessRoutes(workspace = "My Workspace", channel = "alerts"))
        client.configure("test-key", false)
        val result = client.sendAsync(Event(message = "hello"))
        assertTrue(result.isSuccess)
        assertEquals("My Workspace", result.getOrNull()?.workspace)
        assertEquals("alerts", result.getOrNull()?.channel)
        assertTrue(result.getOrNull()?.warnings?.isEmpty() ?: false)
    }

    @Test
    fun `sendAsync returns warnings from response`() = runTest {
        val client = ClientImpl(
            api = SuccessRoutes(warnings = listOf("unknown field: foo", "tag limit reached"))
        )
        client.configure("test-key", false)
        val result = client.sendAsync(Event(message = "hello"))
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.warnings?.size)
        assertEquals("unknown field: foo", result.getOrNull()?.warnings?.get(0))
    }

    @Test
    fun `sendAsync returns failure on generic exception`() = runTest {
        val client = ClientImpl(api = ThrowingRoutes(RuntimeException("unexpected")))
        client.configure("test-key", false)
        val result = client.sendAsync(Event(message = "hello"))
        assertTrue(result.isFailure)
        assertIs<ApiAlertsException>(result.exceptionOrNull())
        assertEquals("invalid response from server", result.exceptionOrNull()?.message)
    }

    // --- sendWithKeyAsync tests ---

    @Test
    fun `sendWithKeyAsync returns failure for blank api key`() = runTest {
        val client = ClientImpl(api = SuccessRoutes())
        val result = client.sendWithKeyAsync("", Event(message = "hello"))
        assertTrue(result.isFailure)
        assertEquals("api key is missing", result.exceptionOrNull()?.message)
    }

    @Test
    fun `sendWithKeyAsync returns failure for blank message`() = runTest {
        val client = ClientImpl(api = SuccessRoutes())
        val result = client.sendWithKeyAsync("test-key", Event(message = ""))
        assertTrue(result.isFailure)
        assertEquals("message is required", result.exceptionOrNull()?.message)
    }

    @Test
    fun `sendWithKeyAsync returns success`() = runTest {
        val client = ClientImpl(api = SuccessRoutes(workspace = "Other Workspace", channel = "dev"))
        val result = client.sendWithKeyAsync("other-key", Event(message = "hello"))
        assertTrue(result.isSuccess)
        assertEquals("Other Workspace", result.getOrNull()?.workspace)
        assertEquals("dev", result.getOrNull()?.channel)
    }
}
