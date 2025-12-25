package com.apialerts.client.util

internal sealed class ResourceResult<T> {
    class Success<T>(val data: T) : ResourceResult<T>()
    class Error<T>(val error: ErrorObject) : ResourceResult<T>()
}