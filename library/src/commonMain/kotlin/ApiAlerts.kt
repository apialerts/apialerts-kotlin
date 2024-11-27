package com.apialerts

class ApiAlerts private constructor() {

    private val client: Client = ClientImpl()

    companion object {
        private val alerts: ApiAlerts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiAlerts() }

        /**
         * Configure the ApiAlerts client
         *
         * @param apiKey String Workspace API key
         * @param debug Boolean Set to true to enable debug logging
         */
        fun configure(apiKey: String, debug: Boolean = false) {
            alerts.client.configure(apiKey, debug)
        }

        /**
         * Send an alert
         *
         * @param apiKey String? Uses the default Workspace API key if not provided.
         * @param channel String Optional channel to send the alert to. Uses the default channel set if not provided.
         * @param message String The message to send.
         * @param tags List<String>? Optional tags to attach to the alert.
         * @param link String? Optional link to attach to the alert.
         */
        fun send(apiKey: String? = null, channel: String? = null, message: String, tags: List<String>? = null, link: String? = null) {
            alerts.client.send(
                apiKey = apiKey,
                channel = channel,
                message = message,
                tags = tags,
                link = link
            )
        }

        /**
         * Send an alert - Async
         *
         * @param apiKey String? Uses the default Workspace API key if not provided.
         * @param channel String Optional channel to send the alert to. Uses the default channel set if not provided.
         * @param message String The message to send.
         * @param tags List<String>? Optional tags to attach to the alert.
         * @param link String? Optional link to attach to the alert.
         */
        suspend fun sendAsync(apiKey: String? = null, channel: String? = null, message: String, tags: List<String>? = null, link: String? = null) {
            alerts.client.sendAsync(
                apiKey = apiKey,
                channel = channel,
                message = message,
                tags = tags,
                link = link
            )
        }

        /**
         * DSL function to send an alert
         * @param block SendRequestBuilder.() -> Unit
         */
        fun send(block: SendRequestBuilder.() -> Unit) {
            val builder = SendRequestBuilder().apply(block)
            alerts.client.send(
                apiKey = builder.apiKey,
                channel = builder.channel,
                message = builder.message,
                tags = builder.tags,
                link = builder.link
            )
        }

        /**
         * DSL function to send an alert - Async
         * @param block SendRequestBuilder.() -> Unit
         */
        fun sendAsync(block: SendRequestBuilder.() -> Unit) {
            val builder = SendRequestBuilder().apply(block)
            alerts.client.send(
                apiKey = builder.apiKey,
                channel = builder.channel,
                message = builder.message,
                tags = builder.tags,
                link = builder.link
            )
        }
    }
}

class SendRequestBuilder {
    /**
     * Uses the default Workspace API key if not provided.
     */
    var apiKey: String? = null

    /**
     * Optional channel to send the alert to. Uses the default channel set if not provided.
     */
    var channel: String? = null

    /**
     * The message to send.
     */
    lateinit var message: String

    /**
     * Optional Tags to attach to the alert.
     */
    var tags: List<String>? = null

    /**
     * Optional link to attach to the alert.
     */
    var link: String? = null
}
