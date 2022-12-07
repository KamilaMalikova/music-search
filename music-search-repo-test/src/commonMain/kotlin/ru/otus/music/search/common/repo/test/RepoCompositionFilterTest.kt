package ru.otus.music.search.common.repo.test

import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.repo.CompositionFilterDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoCompositionFilterTest {
    abstract val repo: ICompositionRepository

    protected open val initializedObjects: List<MsCompositionDiscussion> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.filter(CompositionFilterDbRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1].copy(comments = mutableSetOf()), initializedObjects[3].copy(comments = mutableSetOf())).sortedBy { it.composition.id.asString() }
        assertEquals(expected, result.data?.map { it.copy(comments = mutableSetOf()) }?.toSet()?.sortedBy { it.composition.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchStatus() = runRepoTest {
        val result = repo.filter(CompositionFilterDbRequest(status = status))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2].copy(comments = mutableSetOf()), initializedObjects[4].copy(comments = mutableSetOf())).sortedBy { it.composition.id.asString() }
        assertEquals(expected, result.data?.map { it.copy(comments = mutableSetOf()) }?.toSet()?.sortedBy { it.composition.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitCompositions("search") {

        val searchOwnerId = MsUserId("owner-124")
        val status = MsDiscussionStatus.OPEN

        override val initObjects: List<MsCompositionDiscussion> = listOf(
            createInitCompositionDiscussion("composition-0"),
            createInitCompositionDiscussion("composition-1", ownerId = searchOwnerId),
            createInitCompositionDiscussion("composition-2", status = status),
            createInitCompositionDiscussion("composition-3", ownerId = searchOwnerId),
            createInitCompositionDiscussion("composition-4", status = status),
        )
    }
}
