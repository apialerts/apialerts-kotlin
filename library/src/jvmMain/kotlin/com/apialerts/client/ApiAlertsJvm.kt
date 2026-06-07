package com.apialerts.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * Java helpers. Java can't call `suspend` functions, so this bridges the
 * awaitable send to [CompletableFuture] and provides a factory for the
 * injectable [ApiAlertsClient].
 */
object ApiAlertsJvm {

    /** Construct an injectable [ApiAlertsClient] (e.g. a Spring bean). */
    @JvmStatic
    @JvmOverloads
    fun client(apiKey: String, debug: Boolean = false): ApiAlertsClient =
        ApiAlertsClient(apiKey, debug)

    /** Awaitable send via the singleton. Completes exceptionally with [ApiAlertsException] on failure. */
    @JvmStatic
    @JvmOverloads
    fun sendFuture(event: Event, apiKey: String? = null): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { ApiAlerts.sendAsync(event, apiKey).getOrThrow() }

    /** Awaitable send via an injected [client]. Completes exceptionally with [ApiAlertsException] on failure. */
    @JvmStatic
    @JvmOverloads
    fun sendFuture(client: ApiAlertsClient, event: Event, apiKey: String? = null): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { client.sendAsync(event, apiKey).getOrThrow() }
}
