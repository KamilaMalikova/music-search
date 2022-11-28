package ru.otus.music.search.biz.validation

import MsRepositoryStub
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test
import ru.otus.music.search.common.models.MsSettings

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
        val settings by lazy {
            MsSettings(
                repoTest = MsRepositoryStub()
            )
        }
        val processor by lazy { MsCompositionProcessor(settings) }
    }
}