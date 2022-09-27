package ru.otus.music.search.biz.validation

import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test

class MsCommentTest {
    @Test
    fun `correct compositionId`() =
        validateCorrectCompositionIdNotEmptyWithEmptyCommentId(command, processor)

    @Test
    fun `empty compositionId`() =
        validateIncorrectEmptyCompositionId(command, processor)

    @Test
    fun `correct authorId`() =
        validateCorrectAuthorIdNotEmpty(command, processor)

    @Test
    fun `empty authorId`() =
        validateIncorrectEmptyAuthorId(command, processor)

    @Test
    fun `correct text description`() =
        validateCorrectComment(command, processor)

    @Test
    fun `empty text description`() =
        validateIncorrectEmptyComment(command, processor)

    @Test
    fun `bad symbols in text description`() =
        validateIncorrectNoContentInComment(command, processor)

    @Test
    fun `trim text description`() =
        validateTrimComment(command, processor)

    private companion object {
        val command = MsCommand.COMMENT
        val processor = MsCompositionProcessor()
    }
}