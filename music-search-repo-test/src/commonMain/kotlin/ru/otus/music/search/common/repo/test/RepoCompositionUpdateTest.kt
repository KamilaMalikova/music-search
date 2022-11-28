package ru.otus.music.search.common.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.music.search.common.repo.ICompositionRepository
import kotlin.test.assertEquals
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoCompositionUpdateTest {
    abstract val repo: ICompositionRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = MsCompositionId("repo-update-not-found")
    protected val lockBad = MsCompositionLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MsCompositionLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MsCompositionDiscussion(
            composition = MsComposition(
                id = updateSucc.composition.id
            ),
            status = MsDiscussionStatus.CLOSED,
            lock = initObjects.first().lock
        )
    }
    private val reqUpdateNotFound = MsCompositionDiscussion(
        composition = MsComposition(
            id = updateIdNotFound
        ),
        status = MsDiscussionStatus.CLOSED,
        lock = initObjects.first().lock
    )

    private val reqUpdateConc by lazy {
        MsCompositionDiscussion(
            composition = MsComposition(
                id = updateConc.composition.id
            ),
            status = MsDiscussionStatus.CLOSED,
            lock = lockBad
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateComposition(CompositionDiscussionDbRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.composition.id, result.data?.composition?.id)
        assertEquals(reqUpdateSucc.status, result.data?.status)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateComposition(CompositionDiscussionDbRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateComposition(CompositionDiscussionDbRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc.composition, result.data?.composition)
        assertEquals(updateConc.comments, result.data?.comments)
        assertEquals(updateConc.status, result.data?.status)
    }

    companion object : BaseInitCompositions("update") {
        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}