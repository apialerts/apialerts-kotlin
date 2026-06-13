package com.apialerts.client

import co.touchlab.kermit.Logger
import com.apialerts.client.contract.EventRequest
import com.apialerts.client.routes.EventRoutes
import com.apialerts.client.routes.EventRoutesImpl
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal val logger = Logger.withTag("apialerts")

internal class ClientImpl(
    private val api: EventRoutes = EventRoutesImpl(),
    private val dispatchers: CoroutineDispatcher = Dispatchers.Default,
) : ApiAlertsClient {

    private var defaultKey: String? = null
    private var debug = false
    private var integration = INTEGRATION_NAME
    private var version = VERSION
    private var baseUrl = BASE_URL

    // Client-owned scope for fire-and-forget sends. SupervisorJob so one
    // failure doesn't cancel sibling sends.
    private val scope = CoroutineScope(SupervisorJob() + dispatchers)

    fun configure(apiKey: String) {
        defaultKey = apiKey
    }

    override fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    override fun setOverrides(integration: String, version: String, baseUrl: String) {
        this.integration = integration
        this.version = version
        this.baseUrl = baseUrl
    }

    override fun send(event: Event, apiKey: String?) {
        when (val resolved = resolveKey(apiKey)) {
            is KeyResolution.Error -> logger.e { "x (apialerts.com) Error: ${resolved.message}" }
            is KeyResolution.Ok -> {
                if (event.message.isBlank()) {
                    logger.e { "x (apialerts.com) Error: message is required" }
                    return
                }
                scope.launch {
                    val result = post(resolved.key, event)
                    if (debug) {
                        result.onSuccess { sent ->
                            logger.i { "✓ (apialerts.com) Alert sent to ${sent.workspace} (${sent.channel})" }
                            sent.warnings.forEach { logger.w { "! (apialerts.com) Warning: $it" } }
                        }
                        result.onFailure { e ->
                            logger.e { "x (apialerts.com) Error: ${e.message}" }
                        }
                    }
                }
            }
        }
    }

    override suspend fun sendAsync(event: Event, apiKey: String?): Result<SendResult> {
        val key = when (val resolved = resolveKey(apiKey)) {
            is KeyResolution.Error -> return Result.failure(ApiAlertsException(resolved.message))
            is KeyResolution.Ok -> resolved.key
        }
        if (event.message.isBlank()) {
            return Result.failure(ApiAlertsException("message is required"))
        }
        return post(key, event)
    }

    private sealed class KeyResolution {
        data class Ok(val key: String) : KeyResolution()
        data class Error(val message: String) : KeyResolution()
    }

    private fun resolveKey(apiKey: String?): KeyResolution = when {
        apiKey != null && apiKey.isBlank() -> KeyResolution.Error("api key is missing")
        apiKey != null -> KeyResolution.Ok(apiKey)
        defaultKey.isNullOrBlank() -> KeyResolution.Error("client not configured")
        else -> KeyResolution.Ok(defaultKey!!)
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
                data = event.data?.toJsonObject(),
            )
            val response = api.send(apiKey, payload, integration, version, baseUrl)
            Result.success(
                SendResult(
                    workspace = response.workspace,
                    channel = response.channel,
                    warnings = response.warnings ?: emptyList(),
                ),
            )
        } catch (e: ClientRequestException) {
            val code = e.response.status.value
            val message = when (code) {
                400 -> "bad request"
                401 -> "unauthorized, check your api key"
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
