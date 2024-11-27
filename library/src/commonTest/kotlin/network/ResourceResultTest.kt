package com.apialerts.network

import com.apialerts.network.contract.ErrorResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class ResourceResultTest {

    @Test
    fun `test ResourceResult Success`() {
        val data = "Test Data"
        val result: ResourceResult<String> = ResourceResult.Success(data)
        assertEquals(data, (result as ResourceResult.Success).data)
    }

    @Test
    fun `test ResourceResult Error`() {
        val error = ErrorResponse("Test Error")
        val result: ResourceResult<String> = ResourceResult.Error(error)
        assertEquals(error, (result as ResourceResult.Error).error)
    }
}