package com.apialerts.client

import kotlinx.serialization.json.JsonObject

/**
 * An event to send to the API Alerts platform.
 *
 * Only [message] is required. All other fields are optional.
 *
 * Kotlin callers can use named parameters or the DSL:
 * ```kotlin
 * val event = Event(message = "Deploy complete", channel = "releases")
 * ```
 *
 * Java callers should use [EventBuilder]:
 * ```java
 * Event event = new EventBuilder("Deploy complete").channel("releases").build();
 * ```
 */
data class Event(
    /** Main notification message. Required. */
    val message: String,
    /** Target channel name. */
    val channel: String? = null,
    /** Event key for routing (e.g. `ci.deploy`). */
    val event: String? = null,
    /** Short title shown above the message. */
    val title: String? = null,
    /** Categorisation tags. */
    val tags: List<String>? = null,
    /** URL attached to the notification. */
    val link: String? = null,
    /** Arbitrary JSON metadata. Used as template variables in destination forwarding rules. */
    val data: JsonObject? = null,
)
