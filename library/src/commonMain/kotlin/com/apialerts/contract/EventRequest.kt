package com.apialerts.contract

import kotlinx.serialization.Serializable

@Serializable
internal data class EventRequest(
    val channel: String?,
    val message: String,
    val link: String?,
    val tags: List<String?>?,
)