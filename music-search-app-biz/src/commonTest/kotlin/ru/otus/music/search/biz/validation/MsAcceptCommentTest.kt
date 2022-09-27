package ru.otus.music.search.biz.validation

import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test

class MsAcceptCommentTest {
    @Test
    fun `correct compositionId`() =
        validateCorrectCompositionIdNotEmptyWithNotEmptyCommentId(command, processor)

    @Test
    fun `empty compositionId`() =
        validateIncorrectEmptyCompositionId(command, processor)


    @Test
    fun `correct commentId`() =
        validateCorrectCommentIdNotEmpty(command, processor)

    @Test
    fun `empty commentId`() =
        validateIncorrectEmptyCommentId(command, processor)

    private companion object {
        val command = MsCommand.ACCEPT
        val processor = MsCompositionProcessor()
    }
}