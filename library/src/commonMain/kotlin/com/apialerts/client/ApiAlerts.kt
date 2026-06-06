package com.apialerts.client

import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.serialization.json.JsonObject

/**
 * Global singleton facade over a default [ApiAlertsClient]. Configure once, then
 * send anywhere. For DI, test mocking, or multiple keys, use [ApiAlertsClient]
 * directly. Java callers use the static methods plus [ApiAlertsJvm.sendFuture].
 */
class ApiAlerts private constructor() {

    private val client: ClientImpl = ClientImpl()

    companion object : ApiAlertsClient {
        private val instance: ApiAlerts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiAlerts() }

        /** Configure the singleton. Calling again replaces the key and debug flag. */
        @JvmStatic
        @JvmOverloads
        fun configure(apiKey: String, debug: Boolean = false) {
            instance.client.configure(apiKey)
            instance.client.setDebug(debug)
        }

        /** Enable or disable debug logging at runtime. Critical errors always log. */
        @JvmStatic
        override fun setDebug(debug: Boolean) {
            instance.client.setDebug(debug)
        }

        /** Override the `X-Integration` / `X-Version` headers and base URL. Internal use. */
        @JvmStatic
        override fun setOverrides(integration: String, version: String, baseUrl: String) {
            instance.client.setOverrides(integration, version, baseUrl)
        }

        /** Fire-and-forget send. Never throws. [apiKey] overrides the configured key for this call. */
        @JvmStatic
        override fun send(event: Event, apiKey: String?) {
            instance.client.send(event, apiKey)
        }

        /** Java convenience: fire-and-forget with no key override. */
        @JvmStatic
        fun send(event: Event) {
            instance.client.send(event, null)
        }

        /** Fire-and-forget DSL form. */
        override fun send(block: EventScope.() -> Unit) {
            instance.client.send(EventScope().apply(block).build())
        }

        /** Awaitable send. Returns [Result] with [SendResult] or [ApiAlertsException]; never throws. */
        @JvmStatic
        override suspend fun sendAsync(event: Event, apiKey: String?): Result<SendResult> {
            return instance.client.sendAsync(event, apiKey)
        }

        /** Awaitable DSL form. */
        override suspend fun sendAsync(block: EventScope.() -> Unit): Result<SendResult> {
            return instance.client.sendAsync(EventScope().apply(block).build())
        }
    }
}

/** Builder for the [ApiAlerts.send] / [ApiAlerts.sendAsync] DSL forms. */
class EventScope {
    /** Required. Human-readable notification text. */
    lateinit var message: String

    /** Workspace channel the push notification fires on. */
    var channel: String? = null

    /** Event key for routing rules (e.g. `ci.deploy.success`). */
    var event: String? = null

    /** Short headline some destinations render separately from the message body. */
    var title: String? = null

    /** Categorisation tags for filtering and search. */
    var tags: List<String>? = null

    /** URL associated with the event (deeplink + call-to-action). */
    var link: String? = null

    /** Arbitrary key-value metadata for non-push destination templating. */
    var data: JsonObject? = null

    internal fun build() = Event(
        message = message,
        channel = channel,
        event = event,
        title = title,
        tags = tags,
        link = link,
        data = data,
    )
}
