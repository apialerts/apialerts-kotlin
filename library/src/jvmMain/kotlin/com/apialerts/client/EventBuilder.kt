package com.apialerts.client

/**
 * Fluent builder for [Event]. Intended for Java callers, where named
 * parameters are unavailable - Kotlin callers should use the [Event] data
 * class or the `ApiAlerts.send { ... }` DSL directly.
 *
 * Only `message` is required. All other fields are optional and omitted
 * from the request body when not set.
 *
 * ```java
 * Event event = new EventBuilder("Deploy complete")
 *     .channel("releases")
 *     .event("ci.deploy.success")
 *     .title("Deployed")
 *     .tags(List.of("CI/CD", "Java"))
 *     .link("https://github.com/apialerts/apialerts-kotlin/actions")
 *     .build();
 * ```
 *
 * Attach a `data` payload with a plain `Map`:
 * ```java
 * Event event = new EventBuilder("Deploy complete")
 *     .data(Map.of("plan", "pro", "count", 5))
 *     .build();
 * ```
 * For full control over the JSON shape, pass a `JsonObject` - it is itself a
 * `Map` and so is accepted by the same `data` method.
 */
class EventBuilder(private val message: String) {

    private var channel: String? = null
    private var event: String? = null
    private var title: String? = null
    private var tags: List<String>? = null
    private var link: String? = null
    private var data: Map<String, Any?>? = null

    /**
     * Workspace channel the push notification fires on. Defaults to the
     * workspace default channel when omitted.
     */
    fun channel(channel: String): EventBuilder = apply { this.channel = channel }

    /**
     * Event key for routing rules. Use dotted notation (e.g.
     * `ci.deploy.success`, `payment.failed`, `user.signup`) so routing rules
     * can match glob patterns like `ci.*` or `*.failed`.
     */
    fun event(event: String): EventBuilder = apply { this.event = event }

    /** Short headline some destinations render separately from the message body. */
    fun title(title: String): EventBuilder = apply { this.title = title }

    /** Categorisation tags for filtering and search. */
    fun tags(tags: List<String>): EventBuilder = apply { this.tags = tags }

    /**
     * URL associated with the event. Available as a deeplink for push
     * notifications and as a call-to-action for routed destinations.
     */
    fun link(link: String): EventBuilder = apply { this.link = link }

    /**
     * Arbitrary key-value metadata. Available to non-push destinations for
     * templating (Slack message bodies, email templates, webhook payloads).
     *
     * Values are mapped to JSON heuristically (strings, numbers, booleans,
     * nested maps/lists, and null). For full control over the JSON shape, pass
     * a `JsonObject` - it is itself a `Map` and so is accepted here directly.
     */
    fun data(data: Map<String, Any?>): EventBuilder = apply { this.data = data }

    /** Build the [Event]. The builder may be reused after `build()`. */
    fun build(): Event = Event(
        message = message,
        channel = channel,
        event = event,
        title = title,
        tags = tags,
        link = link,
        data = data,
    )
}
