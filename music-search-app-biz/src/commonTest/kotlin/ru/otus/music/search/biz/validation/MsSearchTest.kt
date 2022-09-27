package ru.otus.music.search.biz.validation

import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test

class MsSearchTest {
    @Test
    fun `correct ownerId`() =
        validateNotEmptyOwnerIdInFilter(command, processor)

    @Test
    fun `empty ownerId`() =
        validateEmptyOwnerIdInFilter(command, processor)

    private companion object {
        val command = MsCommand.SEARCH
        val processor = MsCompositionProcessor()
    }
}