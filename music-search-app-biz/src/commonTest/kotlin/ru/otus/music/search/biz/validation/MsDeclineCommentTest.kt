package ru.otus.music.search.biz.validation

import MsRepositoryStub
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test
import ru.otus.music.search.common.models.MsSettings

class MsDeclineCommentTest {
    @Test
    fun `correct compositionId`() =
        validateCorrectCompositionIdNotEmptyWithNotEmptyCommentId(
            command,
            processor
        )

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
        val command = MsCommand.DECLINE
        val settings by lazy {
            MsSettings(
                repoTest = MsRepositoryStub()
            )
        }
        val processor by lazy { MsCompositionProcessor(settings) }
    }
}