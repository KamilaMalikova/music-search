package ru.otus.music.search.common.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.repo.CompositionIdDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoCompositionReadTest {
    abstract val repo: ICompositionRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readComposition(CompositionIdDbRequest(readSucc.composition.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc.composition, result.data?.composition)
        assertEquals(readSucc.comments, result.data?.comments)
        assertEquals(readSucc.status, result.data?.status)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readComposition(CompositionIdDbRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitCompositions("delete") {
        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MsCompositionId("repo-read-notFound")
    }
}