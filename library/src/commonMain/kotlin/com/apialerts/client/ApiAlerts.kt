package com.apialerts.client

import kotlinx.serialization.json.JsonObject

class ApiAlerts private constructor() {

    private val client: Client = ClientImpl()

    companion object {
        private val instance: ApiAlerts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiAlerts() }

        /** Configure the global client. Subsequent calls are no-ops. */
        fun configure(apiKey: String, debug: Boolean = false) {
            instance.client.configure(apiKey, debug)
        }

        /** Override integration name, version, and base URL. For first-party integrations only. */
        fun setOverrides(integration: String, version: String, baseUrl: String) {
            instance.client.setOverrides(integration, version, baseUrl)
        }

        /** Fire-and-forget. Logs critical errors; HTTP errors only logged when debug is enabled. */
        fun send(event: Event) {
            instance.client.send(event)
        }

        /** Fire-and-forget DSL. Logs critical errors; HTTP errors only logged when debug is enabled. */
        fun send(block: EventScope.() -> Unit) {
            instance.client.send(EventScope().apply(block).build())
        }

        /** Awaitable send. Never throws — check [SendResult.success] and [SendResult.error]. */
        suspend fun sendAsync(event: Event): SendResult {
            return instance.client.sendAsync(event)
        }

        /** Awaitable send DSL. Never throws — check [SendResult.success] and [SendResult.error]. */
        suspend fun sendAsync(block: EventScope.() -> Unit): SendResult {
            return instance.client.sendAsync(EventScope().apply(block).build())
        }

        /** Awaitable send with an explicit API key override. Never throws — check [SendResult.success]. */
        suspend fun sendWithKeyAsync(apiKey: String, event: Event): SendResult {
            return instance.client.sendWithKeyAsync(apiKey, event)
        }
    }
}

class EventScope {
    lateinit var message: String
    var channel: String? = null
    var event: String? = null
    var title: String? = null
    var tags: List<String>? = null
    var link: String? = null
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
