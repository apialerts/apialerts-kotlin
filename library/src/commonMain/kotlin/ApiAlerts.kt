package com.apialerts

class ApiAlerts private constructor() {

    private val client: Client = ClientImpl()

    companion object {
        private val alerts: ApiAlerts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiAlerts() }

        /**
         * Configure the ApiAlerts client
         * @param apiKey String
         */
        fun configure(apiKey: String, logging: Boolean) {
            alerts.client.configure(apiKey, logging)
        }

        /**
         * Send an alert
         * @param apiKey String? = null
         * @param channel String
         * @param message String
         * @param tags List<String>? = null
         * @param link String? = null
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
    }
}

class SendRequestBuilder {
    /**
     * Workspace API key
     * Not required if already set via ApiAlerts.configure(). Must set one or the other, or both
     */
    var apiKey: String? = null

    /**
     * Optional channel to send the alert to.
     * Uses the default channel set if not provided.
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
