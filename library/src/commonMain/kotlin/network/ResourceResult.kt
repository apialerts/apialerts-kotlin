package com.apialerts.network

import com.apialerts.network.contract.ErrorResponse

internal sealed class ResourceResult<T> {
    class Success<T>(val data: T) : ResourceResult<T>()
    class Error<T>(val error: ErrorResponse) : ResourceResult<T>()
}
