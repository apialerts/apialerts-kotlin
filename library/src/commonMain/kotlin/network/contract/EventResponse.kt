package com.apialerts.network.contract

import kotlinx.serialization.Serializable

@Serializable
internal data class EventResponse(
    val workspace: String? = null,
    val channel: String? = null,
    val remainingQuota: Long? = null,
    val errors: List<String>? = null,
)
