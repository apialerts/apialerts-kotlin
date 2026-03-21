package com.apialerts.client

import kotlinx.serialization.json.JsonObject

/**
 * Fluent builder for [Event] — intended for Java callers where named parameters are unavailable.
 * Kotlin callers should use the [Event] data class or DSL directly.
 *
 * Usage from Java:
 * ```java
 * import com.apialerts.client.ApiAlerts;
 * import com.apialerts.client.EventBuilder;
 * import kotlinx.serialization.json.JsonObject;
 *
 * // Simple event
 * Event event = new EventBuilder("Deploy complete")
 *     .channel("releases")
 *     .event("ci.deploy")
 *     .title("Deployed")
 *     .tags(List.of("CI/CD", "Java"))
 *     .link("https://github.com/apialerts/apialerts-kotlin/actions")
 *     .build();
 *
 * // With metadata (requires kotlinx-serialization-json on the classpath)
 * JsonObject data = JsonObjectKt.buildJsonObject(builder -> {
 *     JsonElementKt.put(builder, "version", "1.0.0");
 *     return null;
 * });
 * Event eventWithData = new EventBuilder("Deploy complete").data(data).build();
 * ```
 */
class EventBuilder(private val message: String) {

    private var channel: String? = null
    private var event: String? = null
    private var title: String? = null
    private var tags: List<String>? = null
    private var link: String? = null
    private var data: JsonObject? = null

    fun channel(channel: String): EventBuilder = apply { this.channel = channel }
    fun event(event: String): EventBuilder = apply { this.event = event }
    fun title(title: String): EventBuilder = apply { this.title = title }
    fun tags(tags: List<String>): EventBuilder = apply { this.tags = tags }
    fun link(link: String): EventBuilder = apply { this.link = link }
    fun data(data: JsonObject): EventBuilder = apply { this.data = data }

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
