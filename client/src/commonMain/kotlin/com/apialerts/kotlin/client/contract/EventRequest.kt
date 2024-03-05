package com.apialerts.kotlin.client.contract

import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val message: String? = null,
    val link: String? = null,
    val tags: List<String?>? = null,
)