package ru.otus.music.search.biz.stubs

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.stubs.MsStub
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MsReadDiscussionStubTest {
    @Test
    fun read() = runTest {
        val ctx = MsContext(
            command = MsCommand.READ,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.SUCCESS,
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(id = id)
            )
        )
        processor.exec(ctx)
        assertEquals(id, ctx.compositionResponse.composition.id)
        assertEquals(MsDiscussionStatus.OPEN, ctx.compositionResponse.status)
    }

    @Test
    fun badId() = runTest {
        val ctx = MsContext(
            command = MsCommand.READ,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.BAD_ID,
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(
                    id = MsCompositionId.NONE
                )
            )
        )
        processor.exec(ctx)
        assertEquals(MsCompositionDiscussion(), ctx.compositionResponse)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
        assertEquals("validation-id", ctx.errors.firstOrNull()?.code)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("Wrong id field", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun dbError() = runTest {
        val ctx = MsContext(
            command = MsCommand.READ,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.DB_ERROR,
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(
                    id = id,
                    owner = MsUserId.NONE
                )
            )
        )
        processor.exec(ctx)
        assertEquals(MsCompositionDiscussion(), ctx.compositionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
        assertEquals("internal error", ctx.errors.firstOrNull()?.message)
    }

    @Test
    fun noStubCase() = runTest {
        val ctx = MsContext(
            command = MsCommand.READ,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.NONE,
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(
                    id = id,
                    owner = MsUserId.NONE
                )
            )
        )
        processor.exec(ctx)
        assertEquals(MsCompositionDiscussion(), ctx.compositionResponse)

        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun noDiscussionFound() = runTest {
        val ctx = MsContext(
            command = MsCommand.READ,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.NOT_FOUND,
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(
                    id = id
                )
            )
        )
        processor.exec(ctx)
        assertEquals(MsCompositionDiscussion(), ctx.compositionResponse)

        assertEquals("validation", ctx.errors.firstOrNull()?.group)
        assertEquals("validation-not-found", ctx.errors.firstOrNull()?.code)
        assertEquals("Composition is not found", ctx.errors.firstOrNull()?.message)
    }

    private companion object {
        val processor: MsCompositionProcessor = MsCompositionProcessor()
        val id = MsCompositionId("1234")
    }
}