package ru.otus.music.search.biz.validation

import MsRepositoryStub
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand
import kotlin.test.Test
import ru.otus.music.search.common.models.MsSettings

class MsReadDiscussionTest {
    @Test
    fun `correct compositionId`() =
        validateCorrectCompositionIdNotEmptyWithEmptyCommentId(command, processor)

    @Test
    fun `empty compositionId`() =
        validateIncorrectEmptyCompositionId(command, processor)

    private companion object {
        val command = MsCommand.COMMENT
        val settings by lazy {
            MsSettings(
                repoTest = MsRepositoryStub()
            )
        }
        val processor by lazy { MsCompositionProcessor(settings) }
    }
}