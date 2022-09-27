package ru.otus.music.search.biz.validation

import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test

class MsReadDiscussionTest {
    @Test
    fun `correct compositionId`() =
        validateCorrectCompositionIdNotEmptyWithEmptyCommentId(command, processor)

    @Test
    fun `empty compositionId`() =
        validateIncorrectEmptyCompositionId(command, processor)

    private companion object {
        val command = MsCommand.COMMENT
        val processor = MsCompositionProcessor()
    }
}