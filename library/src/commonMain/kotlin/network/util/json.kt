package com.apialerts.network.util

import kotlinx.serialization.json.Json

internal val json = Json {
    isLenient = false
    ignoreUnknownKeys = true
    coerceInputValues = true
    encodeDefaults = true
}