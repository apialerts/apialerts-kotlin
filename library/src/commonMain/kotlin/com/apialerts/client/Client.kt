package com.apialerts.client

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.routes.EventRoutes
import com.apialerts.client.routes.EventRoutesImpl
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal interface Client {
    fun configure(apiKey: String, debug: Boolean)
    fun setOverrides(integration: String, version: String, baseUrl: String)
    fun send(event: Event)
    suspend fun sendAsync(event: Event): Result<SendResult>
    suspend fun sendWithKeyAsync(apiKey: String, event: Event): Result<SendResult>
}

internal class ClientImpl(
    private val api: EventRoutes = EventRoutesImpl(),
    private val dispatchers: CoroutineDispatcher = Dispatchers.Default,
) : Client {

    private var defaultKey: String? = null
    private var debug = false
    private var integration = INTEGRATION_NAME
    private var version = VERSION
    private var baseUrl = BASE_URL

    override fun configure(apiKey: String, debug: Boolean) {
        defaultKey = apiKey
        this.debug = debug
    }

    override fun setOverrides(integration: String, version: String, baseUrl: String) {
        this.integration = integration
        this.version = version
        this.baseUrl = baseUrl
    }

    override fun send(event: Event) {
        // Critical errors — always log regardless of debug setting
        val key = defaultKey
        if (key == null) {
            println("x (apialerts.com) Error: client not configured")
            return
        }
        if (event.message.isBlank()) {
            println("x (apialerts.com) Error: message is required")
            return
        }
        CoroutineScope(dispatchers).launch {
            val result = post(key, event)
            if (debug) {
                result.onSuccess { sent ->
                    println("✓ (apialerts.com) Alert sent to ${sent.workspace} (${sent.channel})")
                    sent.warnings.forEach { println("! (apialerts.com) Warning: $it") }
                }
                result.onFailure { e ->
                    println("x (apialerts.com) Error: ${e.message}")
                }
            }
        }
    }

    override suspend fun sendAsync(event: Event): Result<SendResult> {
        val key = defaultKey
            ?: return Result.failure(ApiAlertsException("client not configured"))
        if (event.message.isBlank()) {
            return Result.failure(ApiAlertsException("message is required"))
        }
        return post(key, event)
    }

    override suspend fun sendWithKeyAsync(apiKey: String, event: Event): Result<SendResult> {
        if (apiKey.isBlank()) {
            return Result.failure(ApiAlertsException("api key is missing"))
        }
        if (event.message.isBlank()) {
            return Result.failure(ApiAlertsException("message is required"))
        }
        return post(apiKey, event)
    }

    private suspend fun post(apiKey: String, event: Event): Result<SendResult> {
        return try {
            val payload = EventRequest(
                message = event.message,
                channel = event.channel,
                event = event.event,
                title = event.title,
                tags = event.tags,
                link = event.link,
                data = event.data,
            )
            val response = api.send(apiKey, payload, integration, version, baseUrl)
            Result.success(SendResult(
                workspace = response.workspace,
                channel = response.channel,
                warnings = response.warnings ?: emptyList(),
            ))
        } catch (e: ClientRequestException) {
            val code = e.response.status.value
            val message = when (code) {
                400 -> "bad request"
                401 -> "unauthorized — check your api key"
                403 -> "forbidden"
                429 -> "rate limit exceeded"
                else -> "unexpected status: $code"
            }
            Result.failure(ApiAlertsException(message))
        } catch (e: Exception) {
            Result.failure(ApiAlertsException("invalid response from server"))
        }
    }
}
