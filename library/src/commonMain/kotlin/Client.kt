package com.apialerts

import com.apialerts.network.contract.EventRequest
import com.apialerts.network.Network
import com.apialerts.network.NetworkImpl
import com.apialerts.network.ResourceResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal interface Client {
    fun configure(apiKey: String, debug: Boolean)
    fun send(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?)
    suspend fun sendAsync(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?)
}

internal class ClientImpl : Client {

    private val api: Network = NetworkImpl()

    private val dispatchers = Dispatchers.IO

    // Default API Key to use for all send requests
    private var defaultKey: String? = null
    private var showLogs = false

    // Set the default API Key to use for all send requests
    override fun configure(apiKey: String, debug: Boolean) {
        defaultKey = apiKey
        showLogs = debug
    }

    override fun send(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?) {
        CoroutineScope(dispatchers).launch {
            sendAsync(apiKey, channel, message, tags, link)
        }
    }

    override suspend fun sendAsync(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?) {
        val useKey = apiKey ?: this.defaultKey

        if (useKey == null) {
            println("APIAlerts -> API Key not provided. Use configure() to set a default key, or pass the key as a parameter to the send/sendAsync function.")
            return
        }

        if (message.isBlank()) {
            println("APIAlerts -> Message is required")
            return
        }

        val payload = EventRequest(
            channel = channel,
            message = message,
            tags = tags,
            link = link
        )

        when(val response = api.send(useKey, payload)) {
            is ResourceResult.Success -> {
                if (showLogs) {
                    println("✓ (apialerts.com) Alert sent to ${response.data.workspace} (${response.data.channel}) successfully.")
                    response.data.errors?.forEach { item ->
                        println("APIAlerts -> Warning: $item")
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