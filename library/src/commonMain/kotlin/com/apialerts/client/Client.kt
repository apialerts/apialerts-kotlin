package com.apialerts.client

import com.apialerts.client.contract.EventRequest
import com.apialerts.client.routes.EventRoutes
import com.apialerts.client.routes.EventRoutesImpl
import com.apialerts.client.util.ResourceResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal interface Client {
    /**
     * Configures the client with a default API key and debug settings.
     * This is useful for setting a single key for the application lifecycle.
     *
     * @param apiKey The default API key to use for all requests.
     * @param debug Enables or disables debug logging to the console.
     */
    fun configure(apiKey: String, debug: Boolean)

    /**
     * Sends an alert in a "fire-and-forget" manner.
     * This function returns immediately and performs the network request in the background.
     * This is the recommended method for most client-side applications (e.g., Android, iOS, JS, Desktop).
     *
     * @param apiKey The API key to use for this request. If null, the default key from [configure] is used.
     * @param channel The channel to send the alert to. If null, the default channel for the API key is used.
     * @param message The content of the alert.
     * @param tags A list of tags to associate with the alert.
     * @param link A URL to include with the alert.
     */
    fun send(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?)

    /**
     * Sends an alert and suspends until the network request is complete.
     * This is useful in environments where you need to ensure the request has finished before proceeding,
     * such as in a serverless function or a backend script.
     *
     * @param apiKey The API key to use for this request. If null, the default key from [configure] is used.
     * @param channel The channel to send the alert to. If null, the default channel for the API key is used.
     * @param message The content of the alert.
     * @param tags A list of tags to associate with the alert.
     * @param link A URL to include with the alert.
     */
    suspend fun sendAsync(apiKey: String?, channel: String?, message: String, tags: List<String>?, link: String?)
}

internal class ClientImpl : Client {

    private val api: EventRoutes = EventRoutesImpl()

    private val dispatchers = Dispatchers.Default

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