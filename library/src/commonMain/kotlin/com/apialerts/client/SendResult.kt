package com.apialerts.client

/**
 * The outcome of a successful event delivery returned by
 * [ApiAlerts.sendAsync]. Failures are represented as [ApiAlertsException]
 * inside [Result.failure] rather than as fields on this type.
 *
 * ```kotlin
 * val result = ApiAlerts.sendAsync(event)
 * result.onSuccess { sent -> println("Sent to ${sent.workspace} (${sent.channel})") }
 * result.onFailure { e -> println("Error: ${e.message}") }
 * ```
 */
data class SendResult(
    /** Workspace name returned by the server. `null` if the server omitted it. */
    val workspace: String?,
    /** Channel the event landed on. `null` if the server omitted it. */
    val channel: String?,
    /**
     * Non-fatal warnings returned by the server (e.g. deprecated field
     * usage). Empty when there are none.
     */
    val warnings: List<String> = emptyList(),
)
