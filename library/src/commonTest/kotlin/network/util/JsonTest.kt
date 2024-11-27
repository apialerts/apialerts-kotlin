package network.util

import com.apialerts.network.util.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Serializable
data class TestData(val id: Int, val name: String = "default")

class JsonUtilTest {

    @Test
    fun `test json serialization with defaults`() {
        val data = TestData(id = 1)
        val jsonString = json.encodeToString(data)
        assertEquals("""{"id":1,"name":"default"}""", jsonString)
    }

    @Test
    fun `test json deserialization with unknown keys`() {
        val jsonString = """{"id":1,"name":"test","extra":"value"}"""
        val data = json.decodeFromString<TestData>(jsonString)
        assertEquals(1, data.id)
        assertEquals("test", data.name)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun `test json deserialization with missing non-default key`() {
        val jsonString = """{"name":"test"}"""
        assertFailsWith<MissingFieldException> {
            json.decodeFromString<TestData>(jsonString)
        }
    }

    @Test
    fun `test json deserialization with missing default key`() {
        val jsonString = """{"id":1}"""
        val data = json.decodeFromString<TestData>(jsonString)
        assertEquals(1, data.id)
        assertEquals("default", data.name)
    }
}