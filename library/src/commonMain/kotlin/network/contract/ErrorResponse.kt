package com.apialerts.network.contract

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    var message: String? = null,
)
