package ru.otus.music.search.repo

import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.repo.inmemory.CompositionRepoInMemory
import ru.otus.music.search.module

class V1AdMockApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidSup = "10000000-0000-0000-0000-000000000003"
    private val initComposition = MsCompositionDiscussionStub.prepareResult {
        composition = MsComposition(
            id = MsCompositionId(uuidOld),
            owner = MsUserId("user 1234")
        )
        comment = MsComment(
            author = MsUserId("user 123"),
            text = "Comment"
        )
        lock = MsCompositionLock(uuidOld)
    }

    @Test
    fun createComposition() = testApplication {
        val repo = CompositionRepoInMemory(initObjects = listOf(initComposition), randomUuid = { uuidNew })
        val settings = MsSettings(repoTest = repo)
        application {
            module(settings)
        }

//        val client = myClient()
    }
}