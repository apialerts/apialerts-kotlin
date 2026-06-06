package com.apialerts.client

/**
 * Instance-based client. Inject it (Koin, Hilt, Spring, ...) when you want
 * lifecycle control, test mocking, or multiple keys. [ApiAlerts] is a thin
 * singleton facade over a default instance of this interface.
 */
interface ApiAlertsClient {

    /** Enable or disable debug logging at runtime. Critical errors always log. */
    fun setDebug(debug: Boolean)

    /** Override the `X-Integration` / `X-Version` headers and base URL. Internal use. */
    fun setOverrides(integration: String, version: String, baseUrl: String)

    /** Fire-and-forget send. Never throws. [apiKey] overrides the configured key for this call. */
    fun send(event: Event, apiKey: String? = null)

    /** Awaitable send. Returns [Result] with [SendResult] or [ApiAlertsException]; never throws. */
    suspend fun sendAsync(event: Event, apiKey: String? = null): Result<SendResult>

    /** Fire-and-forget DSL form. */
    fun send(block: EventScope.() -> Unit) = send(EventScope().apply(block).build())

    /** Awaitable DSL form. */
    suspend fun sendAsync(block: EventScope.() -> Unit): Result<SendResult> =
        sendAsync(EventScope().apply(block).build())

    companion object {
        /** Construct a configured client to bind in your DI graph. */
        operator fun invoke(apiKey: String, debug: Boolean = false): ApiAlertsClient =
            ClientImpl().apply {
                configure(apiKey)
                setDebug(debug)
            }
    }
}
