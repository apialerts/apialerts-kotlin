package com.apialerts.client.contract

import kotlinx.serialization.Serializable

@Serializable
internal data class EventResponse(
    val workspace: String? = null,
    val channel: String? = null,
    val warnings: List<String>? = null,
)
