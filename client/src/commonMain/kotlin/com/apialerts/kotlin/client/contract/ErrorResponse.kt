package com.apialerts.kotlin.client.contract

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    var message: String? = null,
)
