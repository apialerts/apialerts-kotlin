package com.apialerts.client

/**
 * The result of a successful event delivery.
 *
 * ```kotlin
 * val result = ApiAlerts.sendAsync(event)
 * result.onSuccess { println("Sent to ${it.workspace} (${it.channel})") }
 * result.onFailure { println("Error: ${it.message}") }
 * ```
 */
data class SendResult(
    /** Workspace name returned by the server. */
    val workspace: String,
    /** Channel name the event was delivered to. */
    val channel: String,
    /** Server-side validation notices (non-fatal). */
    val warnings: List<String> = emptyList(),
)
