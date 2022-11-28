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
abstract class RepoDeleteCommentTest {
    abstract val repo: ICompositionRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteComment(
            CommentIdDbRequest(deleteSucc.composition.id, deleteSucc.comments.first().id, lock = deleteSucc.lock)
        )

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(false, result.data?.comments?.contains(deleteSucc.comments.first()))
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteComment(
            CommentIdDbRequest(notFoundId, notFoundCommentId, lock = lockOld)
        )

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteComment(
            CommentIdDbRequest(deleteConc.composition.id, deleteConc.comments.first().id, lock = lockBad)
        )

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitCompositions("delete") {
        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitCompositionDiscussion("delete"),
            createInitCompositionDiscussion("deleteLock"),
        )
        val notFoundId = MsCompositionId("repo-delete-notFound")
        val notFoundCommentId = MsCommentId("repo-comment-delete-notFound")
    }
}