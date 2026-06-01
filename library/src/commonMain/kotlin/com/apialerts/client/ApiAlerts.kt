package com.apialerts.client

import kotlinx.serialization.json.JsonObject

/**
 * Global API Alerts singleton. Call [configure] once at startup, then call
 * [send] / [sendAsync] anywhere in your codebase. There is no public instance
 * client - this static surface is the entire SDK API.
 *
 * Java callers use the same singleton via the static methods on
 * [ApiAlerts.Companion], plus the [ApiAlertsJvm.sendFuture] helper for
 * `CompletableFuture`-based delivery (since `suspend` functions don't bridge
 * cleanly to Java).
 */
class ApiAlerts private constructor() {

    private val client: Client = ClientImpl()

    companion object {
        private val instance: ApiAlerts by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiAlerts() }

        /**
         * Initialise the singleton with your workspace API key. The first call
         * wins; subsequent calls are no-ops, so configure once at startup.
         *
         * @param apiKey Workspace API key (Bearer token sent on every request).
         * @param debug When `true`, success / warning / error messages are
         *   logged via [co.touchlab.kermit.Logger]. Critical errors (missing
         *   key, not configured) always log regardless.
         */
        fun configure(apiKey: String, debug: Boolean = false) {
            instance.client.configure(apiKey, debug)
        }

        /**
         * Override the `X-Integration` / `X-Version` headers and the base URL.
         *
         * For wrapper libraries that build on top of this SDK to identify
         * themselves (e.g. a Ktor server plugin tagging itself as `ktor-apialerts`).
         * If a wrapper misbehaves we can tell which library is responsible and
         * report it back to its maintainer. Also used in tests to redirect at a
         * mock server. Must be called after [configure].
         */
        fun setOverrides(integration: String, version: String, baseUrl: String) {
            instance.client.setOverrides(integration, version, baseUrl)
        }

        /**
         * Fire-and-forget delivery. Returns immediately; the HTTP request runs
         * in the background. Never throws - errors are silently dropped (or
         * logged when `debug` is enabled). Use [sendAsync] when you need to
         * inspect the result.
         *
         * @param event The event to deliver. Only [Event.message] is required.
         * @param apiKey Optional one-shot override of the configured key.
         *   Useful for sending to multiple workspaces from the same process.
         */
        fun send(event: Event, apiKey: String? = null) {
            instance.client.send(event, apiKey)
        }

        /**
         * Fire-and-forget DSL form. Build the [Event] inline:
         *
         * ```kotlin
         * ApiAlerts.send {
         *     message = "Deploy complete"
         *     channel = "releases"
         * }
         * ```
         */
        fun send(block: EventScope.() -> Unit) {
            instance.client.send(EventScope().apply(block).build())
        }

        /**
         * Awaitable delivery. Returns [Result.success] with [SendResult] on
         * delivery, or [Result.failure] with [ApiAlertsException] on any error.
         * Never throws - the failure case is encoded in [Result].
         *
         * @param event The event to deliver. Only [Event.message] is required.
         * @param apiKey Optional one-shot override of the configured key.
         *   Useful for sending to multiple workspaces from the same process.
         */
        suspend fun sendAsync(event: Event, apiKey: String? = null): Result<SendResult> {
            return instance.client.sendAsync(event, apiKey)
        }

        /**
         * Awaitable DSL form. Build the [Event] inline:
         *
         * ```kotlin
         * val result = ApiAlerts.sendAsync {
         *     message = "Deploy complete"
         *     channel = "releases"
         * }
         * ```
         */
        suspend fun sendAsync(block: EventScope.() -> Unit): Result<SendResult> {
            return instance.client.sendAsync(EventScope().apply(block).build())
        }
    }
}

/**
 * Builder receiver for the [ApiAlerts.send] / [ApiAlerts.sendAsync] DSL forms.
 *
 * Set [message] (required) and any optional fields, then the surrounding
 * [send] / [sendAsync] call builds the [Event] for you.
 */
class EventScope {
    /** Required. Human-readable notification text. */
    lateinit var message: String

    /** Workspace channel the push notification fires on. */
    var channel: String? = null

    /** Event key for routing rules (e.g. `ci.deploy.success`). */
    var event: String? = null

    /** Short headline some destinations render separately from the message body. */
    var title: String? = null

    /** Categorisation tags for filtering and search. */
    var tags: List<String>? = null

    /** URL associated with the event (deeplink + call-to-action). */
    var link: String? = null

    /** Arbitrary key-value metadata for non-push destination templating. */
    var data: JsonObject? = null

    internal fun build() = Event(
        message = message,
        channel = channel,
        event = event,
        title = title,
        tags = tags,
        link = link,
        data = data,
    )
}
