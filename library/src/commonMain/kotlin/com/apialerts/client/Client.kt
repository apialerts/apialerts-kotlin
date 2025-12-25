package com.apialerts.client

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.routes.EventRoutes
import com.apialerts.client.routes.EventRoutesImpl
import com.apialerts.client.util.ResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal interface Client {
    fun configure(apiKey: String, debug: Boolean)
    fun send(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?)
    suspend fun sendAsync(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?)
}

internal class ClientImpl(
    private val api: EventRoutes = EventRoutesImpl(),
    private val dispatchers: CoroutineDispatcher = Dispatchers.Default
) : Client {

    // Default API Key to use for all send requests
    private var defaultKey: String? = null
    private var debug = false

    // Set the default API Key to use for all send requests
    override fun configure(apiKey: String, debug: Boolean) {
        defaultKey = apiKey
        this.debug = debug
    }

    override fun send(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?) {
        CoroutineScope(dispatchers).launch {
            sendAsync(apiKey, channel, message, tags, link)
        }
    }

    override suspend fun sendAsync(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?) {
        val useKey = apiKey ?: this.defaultKey

        if (useKey == null) {
            println("x (apialerts.com) Error: API Key not provided. Use configure() to set a default key, or pass the key as a parameter to the send/sendAsync function.")
            return
        }

        if (message.isBlank()) {
            println("x (apialerts.com) Error: Message is required")
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
                if (debug) {
                    println("✓ (apialerts.com) Alert sent to ${response.data.workspace} (${response.data.channel}) successfully.")
                    response.data.errors?.forEach { item ->
                        println("! (apialerts.com) Warning: $item")
                    }
                }
            }
            is ResourceResult.Error -> {
                if (debug) {
                    println("x (apialerts.com) Error: ${response.error.message}")
                }
            }
        }
    }
}