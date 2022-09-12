package ru.otus.music.search.mappers.v1

import org.junit.Test
import ru.otus.music.search.api.v1.models.CompositionCreateObject
import ru.otus.music.search.api.v1.models.CompositionCreateRequest
import ru.otus.music.search.api.v1.models.CompositionCreateResponse
import ru.otus.music.search.api.v1.models.CompositionDebug
import ru.otus.music.search.api.v1.models.CompositionRequestDebugMode
import ru.otus.music.search.api.v1.models.CompositionRequestDebugStubs
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsRequestId
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.stubs.MsStub
import java.io.File
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = CompositionCreateRequest(
            requestId = "1234",
            debug = CompositionDebug(
                mode = CompositionRequestDebugMode.STUB,
                stub = CompositionRequestDebugStubs.SUCCESS,
            ),
            composition = CompositionCreateObject(
                file = TEST_FILE
            )
        )

        val context = MsContext()
        context.fromTransport(req)

        assertEquals(MsStub.SUCCESS, context.stubCase)
        assertEquals(MsWorkMode.STUB, context.workMode)
        assertEquals(TEST_FILE, context.msRequest.composition.file)
    }

    @Test
    fun toTransport() {
        val context = MsContext(
            requestId = MsRequestId("1234"),
            command = MsCommand.CREATE,
            compositionResponse = MsCompositionDiscussion(
                composition = MsComposition(id = MsCompositionId("1265"))
            ),
            errors = mutableListOf(
                MsError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = MsState.RUNNING,
        )

        val res = context.toTransport() as CompositionCreateResponse

        assertEquals("1234", res.requestId)
        assertEquals("1265", res.compositionInfo?.id)
        assertEquals(1, res.errors?.size)
        assertEquals("err", res.errors?.firstOrNull()?.code)
        assertEquals("request", res.errors?.firstOrNull()?.group)
        assertEquals("title", res.errors?.firstOrNull()?.field)
        assertEquals("wrong title", res.errors?.firstOrNull()?.message)
    }

    private companion object {
        val TEST_FILE = File("${System.getProperty("user.dir")}/test-file")
    }
}