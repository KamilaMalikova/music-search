package ru.otus.music.search.biz.validation

import MsRepositoryStub
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test
import ru.otus.music.search.common.models.MsSettings

class MsSearchTest {
    @Test
    fun `correct ownerId`() =
        validateNotEmptyOwnerIdInFilter(command, processor)

    @Test
    fun `empty ownerId`() =
        validateEmptyOwnerIdInFilter(command, processor)

    private companion object {
        val command = MsCommand.SEARCH
        val settings by lazy {
            MsSettings(
                repoTest = MsRepositoryStub()
            )
        }
        val processor by lazy { MsCompositionProcessor(settings) }
    }
}