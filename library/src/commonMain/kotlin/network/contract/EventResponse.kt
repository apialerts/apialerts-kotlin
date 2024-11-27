package com.apialerts.network.contract

import kotlinx.serialization.Serializable

@Serializable
internal data class EventResponse(
    val project: String? = null,
    val remainingQuota: Long? = null,
    val errors: List<String>? = null,
)
