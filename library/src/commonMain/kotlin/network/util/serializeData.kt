package com.apialerts.network.util

import kotlinx.serialization.KSerializer

internal fun <T> serializeData(data: T, serializer: KSerializer<T>): String {
    return json.encodeToString(serializer, data)
}