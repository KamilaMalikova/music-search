package ru.otus.music.search.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsFilter
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validateNotEmptyOwnerIdInFilter(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        filterRequest = MsFilter(
            ownerId = MsUserId("12345")
        )
    )

    processor.exec(ctx)
    assertNotEquals(MsState.FAILING, ctx.state)
    assertEquals(MsUserId("12345"), ctx.filterValidated.ownerId)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateEmptyOwnerIdInFilter(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        filterRequest = MsFilter()
    )

    processor.exec(ctx)
    assertNotEquals(MsState.FAILING, ctx.state)
    assertEquals(MsUserId.NONE, ctx.filterValidated.ownerId)
}