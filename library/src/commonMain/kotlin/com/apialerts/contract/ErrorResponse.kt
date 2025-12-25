package com.apialerts.contract

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    var message: String? = null,
)
