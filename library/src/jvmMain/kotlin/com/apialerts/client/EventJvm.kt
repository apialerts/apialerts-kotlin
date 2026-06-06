package com.apialerts.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * Java-friendly delivery helper. Kotlin callers should use [ApiAlerts]
 * directly with `suspend` functions.
 *
 * Java callers cannot easily call `suspend fun sendAsync(...)`, so this
 * helper bridges to [CompletableFuture]:
 *
 * ```java
 * // Fire-and-forget
 * ApiAlerts.send(new EventBuilder("Deploy complete").build());
 *
 * // Awaitable - completes exceptionally with ApiAlertsException on failure
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
     * Awaitable delivery. Returns a [CompletableFuture] that completes with
     * [SendResult] on success or completes exceptionally with
     * [ApiAlertsException] on failure.
     *
     * @param event The event to deliver. Only [Event.message] is required.
     * @param apiKey Optional one-shot override of the configured key.
     *   Useful for sending to multiple workspaces from the same process.
     */
    @JvmStatic
    @JvmOverloads
    fun sendFuture(event: Event, apiKey: String? = null): CompletableFuture<SendResult> =
        CoroutineScope(Dispatchers.IO).future { ApiAlerts.sendAsync(event, apiKey).getOrThrow() }
}
