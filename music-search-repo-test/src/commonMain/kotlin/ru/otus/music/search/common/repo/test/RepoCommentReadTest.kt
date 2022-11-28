package ru.otus.music.search.common.repo.test

import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.repo.CommentIdDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoCommentReadTest {
    abstract val repo: ICompositionRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readComment(CommentIdDbRequest(readSucc.composition.id, readSucc.comments.first().id))
        val expectedComment = readSucc.comments.first()

        assertEquals(true, result.isSuccess)
        assertEquals(expectedComment, result.data?.comment)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readComment(CommentIdDbRequest(notFoundId, notFoundCommentId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitCompositions("read") {
        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitCompositionDiscussion("read")
        )

        val notFoundId = MsCompositionId("repo-read-notFound")
        val notFoundCommentId = MsCommentId("repo-comment-read-notFound")
    }
}