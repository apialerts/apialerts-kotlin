package com.apialerts.network.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
data class TestData(val id: Int, val name: String)

class SerializeDataTest {

    @Test
    fun `test serializeData with TestData`() {
        val data = TestData(id = 1, name = "test")
        val jsonString = serializeData(data, TestData.serializer())
        assertEquals("""{"id":1,"name":"test"}""", jsonString)
    }

    @Test
    fun `test serializeData with String`() {
        val data = "test"
        val jsonString = serializeData(data, String.serializer())
        assertEquals(""""test"""", jsonString)
    }
}