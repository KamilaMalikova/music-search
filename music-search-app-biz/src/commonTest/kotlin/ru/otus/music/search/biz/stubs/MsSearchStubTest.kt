package ru.otus.music.search.biz.stubs

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsFilter
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.stubs.MsStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class MsSearchStubTest {
    @Test
    fun search() = runTest {
        val ctx = MsContext(
            command = MsCommand.SEARCH,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.SUCCESS,
            filterRequest = MsFilter(
                ownerId = owner
            )
        )
        processor.exec(ctx)

        assertTrue(ctx.compositionsResponse.isNotEmpty())

        val first = ctx.compositionsResponse.firstOrNull() ?: fail("Empty response list")
        assertEquals(owner, first.composition.owner)
    }

    @Test
    fun dbError() = runTest {
        val ctx = MsContext(
            command = MsCommand.SEARCH,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.DB_ERROR,
            filterRequest = MsFilter(
                ownerId = owner
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
            command = MsCommand.SEARCH,
            state = MsState.NONE,
            workMode = MsWorkMode.STUB,
            stubCase = MsStub.NONE,
            filterRequest = MsFilter(
                ownerId = owner
            )
        )

        processor.exec(ctx)
        assertEquals(MsCompositionDiscussion(), ctx.compositionResponse)

        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }


    private companion object {
        val processor: MsCompositionProcessor = MsCompositionProcessor()
        val owner = MsUserId("567")
    }
}