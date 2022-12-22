package ru.otus.music.search.common.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.music.search.common.models.*
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository
import java.io.File
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoCompositionCreateTest {
    abstract val repo: ICompositionRepository

    protected open val lockNew: MsCompositionLock = MsCompositionLock("20000000-0000-0000-0000-000000000002")

    protected val createObj = MsCompositionDiscussion(
        composition = MsComposition(
            owner = MsUserId("test user"),
            file = MsFile("file-123"),
        )
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createComposition(CompositionDiscussionDbRequest(createObj))
        val expected = createObj.composition.copy(id = result.data?.composition?.id ?: MsCompositionId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.id, result.data?.composition?.id)
        assertEquals(expected.owner, result.data?.composition?.owner)
        assertEquals(expected.file, result.data?.composition?.file)
        assertEquals(lockNew, result.data?.lock)
    }

    companion object : BaseInitCompositions("create") {
        override val initObjects: List<MsCompositionDiscussion> = emptyList()
    }
}