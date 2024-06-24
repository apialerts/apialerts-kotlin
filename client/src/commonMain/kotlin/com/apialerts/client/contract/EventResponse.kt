package com.apialerts.client.contract

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val project: String? = null,
    val remainingQuota: Long? = null,
    val errors: List<String>? = null,
)
