package com.apialerts.client

/**
 * A single notification dispatched to API Alerts.
 *
 * Only [message] is required. All other fields are optional and omitted
 * from the request body when `null` - they are never serialised as `null`.
 *
 * Kotlin callers can use named parameters or the DSL:
 * ```kotlin
 * val event = Event(message = "Deploy complete", channel = "releases")
 *
 * ApiAlerts.send {
 *     message = "Deploy complete"
 *     channel = "releases"
 * }
 * ```
 *
 * Java callers should use [EventBuilder]:
 * ```java
 * Event event = new EventBuilder("Deploy complete").channel("releases").build();
 * ```
 */
data class Event(
    /**
     * Human-readable notification text. Required. This is what appears on
     * the push notification lock screen.
     */
    val message: String,

    /**
     * Workspace channel the push notification fires on. Defaults to the
     * workspace default channel when omitted.
     */
    val channel: String? = null,

    /**
     * Identifies what kind of thing happened. Optional but recommended.
     * Use dotted notation (e.g. `ci.deploy.success`, `payment.failed`,
     * `user.signup`) so routing rules can match glob patterns like `ci.*`
     * or `*.failed`.
     */
    val event: String? = null,

    /** Short headline some destinations render separately from the message body. */
    val title: String? = null,

    /** Categorisation tags for filtering and search. */
    val tags: List<String>? = null,

    /**
     * URL associated with the event. Available as a deeplink for push
     * notifications and as a call-to-action for routed destinations.
     */
    val link: String? = null,

    /**
     * Arbitrary key-value metadata. Available to non-push destinations for
     * templating (Slack message bodies, email templates, webhook payloads).
     *
     * A plain map - values are mapped to JSON heuristically (strings, numbers,
     * booleans, `null`, nested maps and lists). For full control over the JSON
     * shape, pass a `kotlinx.serialization.json.JsonObject`, which is itself a
     * `Map` and so is accepted here directly:
     * ```kotlin
     * Event(message = "Deploy complete", data = mapOf("env" to "prod", "build" to 42))
     * Event(message = "Deploy complete", data = buildJsonObject { put("env", "prod") })
     * ```
     */
    val data: Map<String, Any?>? = null,
)
