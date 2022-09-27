package ru.otus.music.search.biz.validation

import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test

class MsCreateTest {
    @Test
    fun `compositionId is none`() =
        validateCorrectEmptyCompositionId(command, processor)

    @Test
    fun `correct ownerId`() =
        validateCorrectOwnerId(command, processor)

    @Test
    fun `ownerId empty`() =
        validateEmptyOwnerId(command, processor)

    private companion object {
        val command = MsCommand.CREATE
        val processor = MsCompositionProcessor()
    }
}