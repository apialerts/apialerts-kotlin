package com.apialerts.client

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonConversionsTest {

    @Test
    fun mapsPrimitivesAndNull() {
        val result = mapOf(
            "str" to "pro",
            "int" to 5,
            "bool" to true,
            "nil" to null,
        ).toJsonObject()

        val expected = buildJsonObject {
            put("str", "pro")
            put("int", 5)
            put("bool", true)
            put("nil", JsonNull)
        }
        assertEquals(expected, result)
    }

    @Test
    fun recursesNestedMapsAndLists() {
        val result = mapOf(
            "user" to mapOf("id" to 42),
            "tags" to listOf("a", "b"),
        ).toJsonObject()

        val expected = buildJsonObject {
            putJsonObject("user") { put("id", 42) }
            putJsonArray("tags") {
                add("a")
                add("b")
            }
        }
        assertEquals(expected, result)
    }

    @Test
    fun dslAcceptsDataAsMap() {
        val event = EventScope().apply {
            message = "m"
            data = mapOf("plan" to "pro")
        }.build()

        assertEquals(mapOf("plan" to "pro"), event.data)
    }

    @Test
    fun eventAcceptsPlainMap() {
        val event = Event(message = "m", data = mapOf("plan" to "pro"))

        assertEquals(mapOf("plan" to "pro"), event.data)
    }

    @Test
    fun eventAcceptsJsonObject() {
        val json = buildJsonObject { put("plan", "pro") }
        val event = Event(message = "m", data = json)

        assertEquals(json, event.data)
    }

    @Test
    fun jsonObjectRoundTripsThroughConversion() {
        val json = buildJsonObject { put("plan", "pro") }

        assertEquals(json, json.toJsonObject())
    }
}
