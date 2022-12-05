package ru.otus.music.search.common.repo.test

import org.junit.Test
import ru.otus.music.search.common.repo.ICompositionRepository
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.repo.CommentUpdateDbRequest

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoUpdateCommentTest {
    abstract val repo: ICompositionRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]

    protected val lockBad = MsCompositionLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MsCompositionLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MsCompositionDiscussion(
            composition = MsComposition(
                id = updateSucc.composition.id
            ),
            comment = MsComment(
                id = updateSucc.comments.first().id,
                author = updateSucc.comments.first().author,
                text = updateSucc.comments.first().text,
                lock = updateSucc.comments.first().lock
            ),
            lock = initObjects.first().lock
        )
    }
    private val reqUpdateNotFound = MsCompositionDiscussion(
        composition = MsComposition(
            id = updateSucc.composition.id
        ),
        comment = MsComment(
            id = updateIdNotFound,
            lock = initObjects.first().lock
        ),
        lock = initObjects.first().lock
    )

    private val reqUpdateConc by lazy {
        MsCompositionDiscussion(
            composition = MsComposition(
                id = updateConc.composition.id
            ),
            comment = MsComment(
                id = updateConc.comments.first().id,
                author = updateConc.comments.first().author,
                text = updateConc.comments.first().text,
                lock = lockBad
            ),
            lock = lockBad
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateComment(
            CommentUpdateDbRequest(
                compositionId = reqUpdateSucc.composition.id,
                comment = reqUpdateSucc.comment,
                lock = reqUpdateSucc.lock
            )
        )
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.composition.id, result.data?.composition?.id)
        assertEquals(reqUpdateSucc.comment.status, result.data?.comment?.status)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateComment(
            CommentUpdateDbRequest(
                compositionId = reqUpdateNotFound.composition.id,
                comment = reqUpdateNotFound.comment,
                lock = initObjects.first().lock
            )
        )
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("commentId", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateComment(
            CommentUpdateDbRequest(
                compositionId = reqUpdateConc.composition.id,
                comment = reqUpdateConc.comment,
                lock = lockBad
            )
        )
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitCompositions("update") {
        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitCompositionDiscussion("update"),
            createInitCompositionDiscussion("updateConc"),
        )
        val updateIdNotFound = MsCommentId("repo-update-not-found")
    }
}