package ru.otus.music.search.common.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.music.search.common.models.*
import ru.otus.music.search.common.repo.CommentDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoCommentCreateTest {
    abstract val repo: ICompositionRepository

    protected open val lockNew: MsCompositionLock = MsCompositionLock("20000000-0000-0000-0000-000000000002")

    protected val createObj = initObjects.first()

    @Test
    fun createSuccess() = runRepoTest {

        val result = repo.createComment(
            CommentDbRequest(createObj.composition.id, createObj.comment, createObj.lock)
        )
        val resultComment = result.data?.comments?.firstOrNull()
        val expectedComment = createObj.comment.copy(resultComment?.id ?: MsCommentId.NONE)
        val expected = createObj.copy(comment = expectedComment)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.comment.id, resultComment?.id)
        assertEquals(expected.comment.author, resultComment?.author)
        assertEquals(expected.comment.text, resultComment?.text)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitCompositions("create") {
        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitTestModel("create-comment"),
        )
    }
}