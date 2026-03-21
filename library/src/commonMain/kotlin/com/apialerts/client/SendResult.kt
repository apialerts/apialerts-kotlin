package com.apialerts.client

/**
 * The result of sending an event. Never throws — inspect [success] instead.
 *
 * ```kotlin
 * val result = ApiAlerts.sendAsync(event)
 * if (result.success) {
 *     println("Sent to ${result.workspace} (${result.channel})")
 * } else {
 *     println("Error: ${result.error}")
 * }
 * ```
 */
data class SendResult(
    /** `true` if the event was accepted by the server, `false` on any error. */
    val success: Boolean,
    /** Workspace name returned by the server on success. */
    val workspace: String? = null,
    /** Channel name the event was delivered to on success. */
    val channel: String? = null,
    /** Server-side validation notices (non-fatal). Present even when [success] is `true`. */
    val warnings: List<String> = emptyList(),
    /** Human-readable error description when [success] is `false`. */
    val error: String? = null,
)
