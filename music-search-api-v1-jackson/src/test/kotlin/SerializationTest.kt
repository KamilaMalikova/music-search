package ru.otus.music.search.api.v1

import org.junit.Test
import ru.otus.music.search.api.v1.models.*
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationTest {
    private val create = CompositionCreateRequest(
        requestType = "create",
        debug = CompositionDebug(
            mode = CompositionRequestDebugMode.STUB,
            stub = CompositionRequestDebugStubs.BAD_ID
        ),
        composition = CompositionCreateObject(
            file = TEST_FILE,
            owner = "owner"
        )
    )

    @Test
    fun serializationTest() {
        val json = apiV1Mapper.writeValueAsString(create)
        assertContains(json, Regex("\"owner\":\\s*\"owner\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badId\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserializationTest() {
        val json = apiV1Mapper.writeValueAsString(create)
        val result = apiV1Mapper.readValue(json, IRequest::class.java) as CompositionCreateRequest
        assertEquals(create, result)
    }

    private companion object {
        val TEST_FILE ="${System.getProperty("user.dir")}/test-file"
    }
}