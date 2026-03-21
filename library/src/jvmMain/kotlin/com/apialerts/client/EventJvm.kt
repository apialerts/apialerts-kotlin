package com.apialerts.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * Java-friendly helpers for the API Alerts SDK.
 * Kotlin callers should use [ApiAlerts], [Event], and [EventBuilder] directly.
 *
 * Usage from Java:
 * ```java
 * // Fire-and-forget
 * ApiAlerts.send(new EventBuilder("Deploy complete").build());
 *
 * // CompletableFuture (non-blocking)
 * ApiAlertsJvm.sendFuture(new EventBuilder("Deploy complete").build())
 *     .thenAccept(result -> {
 *         if (result.getSuccess()) {
 *             System.out.println("Sent to " + result.getWorkspace());
 *         } else {
 *             System.out.println("Error: " + result.getError());
 *         }
 *     });
 * ```
 */
object ApiAlertsJvm {

    /**
     * Sends an event and returns a [CompletableFuture] that resolves to [SendResult].
     * Never completes exceptionally — check [SendResult.success] instead.
     */
    @JvmStatic
    fun sendFuture(event: Event): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { ApiAlerts.sendAsync(event) }

    /**
     * Sends an event to a specific workspace and returns a [CompletableFuture] that resolves to [SendResult].
     * Never completes exceptionally — check [SendResult.success] instead.
     */
    @JvmStatic
    fun sendWithKeyFuture(apiKey: String, event: Event): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { ApiAlerts.sendWithKeyAsync(apiKey, event) }
}
