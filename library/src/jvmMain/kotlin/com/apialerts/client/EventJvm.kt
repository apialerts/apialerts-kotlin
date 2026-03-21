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
 * // CompletableFuture (non-blocking) — completes exceptionally with ApiAlertsException on failure
 * ApiAlertsJvm.sendFuture(new EventBuilder("Deploy complete").build())
 *     .thenAccept(result -> {
 *         System.out.println("Sent to " + result.getWorkspace() + " (" + result.getChannel() + ")");
 *     })
 *     .exceptionally(e -> {
 *         System.err.println("Error: " + e.getMessage());
 *         return null;
 *     });
 * ```
 */
object ApiAlertsJvm {

    /**
     * Sends an event and returns a [CompletableFuture] that resolves to [SendResult].
     * Completes exceptionally with [ApiAlertsException] on failure.
     */
    @JvmStatic
    fun sendFuture(event: Event): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { ApiAlerts.sendAsync(event).getOrThrow() }

    /**
     * Sends an event to a specific workspace and returns a [CompletableFuture] that resolves to [SendResult].
     * Completes exceptionally with [ApiAlertsException] on failure.
     */
    @JvmStatic
    fun sendWithKeyFuture(apiKey: String, event: Event): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { ApiAlerts.sendWithKeyAsync(apiKey, event).getOrThrow() }
}
