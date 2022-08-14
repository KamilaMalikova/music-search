package ru.otus.music.search.api.v1

import org.junit.Test
import ru.otus.music.search.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationTest {
    private val create = CompositionCreateRequest(
        requestType = "create",
        debug = CompositionDebug(
            mode = CompositionRequestDebugMode.STUB,
            stub = CompositionRequestDebugStubs.BAD_TITLE
        ),
        composition = CompositionCreateObject(
            fileName = "123"
        )
    )

    @Test
    fun serializationTest() {
        val json = apiV1Mapper.writeValueAsString(create)

        assertContains(json, Regex("\"fileName\":\\s*\"123\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserializationTest() {
        val json = apiV1Mapper.writeValueAsString(create)
        val result = apiV1Mapper.readValue(json, IRequest::class.java) as CompositionCreateRequest
        assertEquals(create, result)
    }
}