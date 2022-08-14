package ru.otus.music.search.api.v1

import org.junit.Test
import ru.otus.music.search.api.v1.models.CompositionCreateResponse
import ru.otus.music.search.api.v1.models.CompositionInfo
import ru.otus.music.search.api.v1.models.IResponse
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = CompositionCreateResponse(
        requestId = "123",
        compositionInfo = CompositionInfo(
            id = "132"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"id\":\\s*\"132\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json,  IResponse::class.java) as CompositionCreateResponse

        assertEquals(response, obj)
    }

}

