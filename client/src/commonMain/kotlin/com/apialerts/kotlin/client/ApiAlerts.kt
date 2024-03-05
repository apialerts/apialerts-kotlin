package com.apialerts.kotlin.client

class ApiAlerts private constructor() {

    private val client = Client()

    companion object {
        private val alerts: ApiAlerts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiAlerts() }

        fun configure(apiKey: String, logging: Boolean) {
            alerts.client.configure(apiKey, logging)
        }

        fun send(apiKey: String? = null, message: String, tags: List<String>? = null, link: String? = null) {
            alerts.client.send(apiKey, message, tags, link)
        }
    }
}