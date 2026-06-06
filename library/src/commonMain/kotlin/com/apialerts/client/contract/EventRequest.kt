package com.apialerts.client.contract

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
internal data class EventRequest(
    val message: String,
    val channel: String? = null,
    val event: String? = null,
    val title: String? = null,
    val tags: List<String>? = null,
    val link: String? = null,
    val data: JsonObject? = null,
)