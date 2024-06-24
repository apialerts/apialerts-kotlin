package com.apialerts.client

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.network.Network
import com.apialerts.client.network.ResourceResult
import com.apialerts.client.network.createHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class Client {

    // Platform specific implementation (Android/JVM)
    private val platform: Platform = getPlatform()

    private val httpClient = createHttpClient(platform.getHttpClient())
    private val api = Network(httpClient)

    private val dispatchers = Dispatchers.IO

    // Default API Key to use for all send requests
    private var defaultKey: String? = null
    private var showLogs = false

    // Set the default API Key to use for all send requests
    fun configure(apiKey: String, logging: Boolean = false) {
        defaultKey = apiKey
        showLogs = logging
    }

    fun send(apiKey: String? = null, message: String, tags: List<String>? = null, link: String? = null) {
        val useKey = apiKey ?: this.defaultKey

        if (useKey == null) {
            println("APIAlerts -> Project API Key not provided. Use configure(apiKey: String) to set a default key, or pass the key as a parameter to the send function.")
            return
        }

        if (message.isBlank()) {
            println("APIAlerts -> Message is required")
            return
        }

        val payload = EventRequest(
            message = message,
            tags = tags,
            link = link
        )
        CoroutineScope(dispatchers).launch {
            when(val response = api.send(useKey, payload)) {
                is ResourceResult.Success -> {
                    if (showLogs) {
                        println("APIAlerts -> Successfully sent event to ${response.data.project ?: "??"}. Remaining Quota = ${response.data.remainingQuota ?: 0}")
                        response.data.errors?.forEach { item ->
                            print("APIAlerts Warning -> $item")
                        }
                    }
                }
                is ResourceResult.Error -> {
                    if (showLogs) {
                        println("APIAlerts -> Error: ${response.error.message}")
                    }
                }
            }
        }
    }
}