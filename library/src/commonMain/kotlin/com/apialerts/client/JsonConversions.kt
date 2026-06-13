package com.apialerts.client

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

/**
 * Convert the [Event.data] map into the [JsonObject] sent on the wire.
 *
 * Called once at send time when building the request payload. Values are
 * mapped heuristically: `String`/`Number`/`Boolean`/`null` become primitives,
 * nested `Map`s and `Iterable`s recurse, an existing [JsonElement] passes
 * through (so a caller-supplied `JsonObject` round-trips unchanged), and
 * anything else falls back to its `toString()`.
 */
internal fun Map<String, Any?>.toJsonObject(): JsonObject =
    buildJsonObject {
        for ((key, value) in this@toJsonObject) put(key, value.toJsonElement())
    }

private fun Any?.toJsonElement(): JsonElement =
    when (this) {
        null -> JsonNull
        is JsonElement -> this
        is String -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is Map<*, *> -> buildJsonObject {
            for ((key, value) in this@toJsonElement) put(key.toString(), value.toJsonElement())
        }
        is Iterable<*> -> buildJsonArray {
            for (value in this@toJsonElement) add(value.toJsonElement())
        }
        else -> JsonPrimitive(toString())
    }
