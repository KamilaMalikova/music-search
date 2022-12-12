package ru.otus.music.search.biz.repo

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.repo.CompositionDbResponse
import ru.otus.music.search.common.repo.test.CompositionRepositoryMock
import java.io.File
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsUserGroups

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoCreateCompositionTest {
    private val command = MsCommand.CREATE
    private val repo = CompositionRepositoryMock(
        invokeCreateComposition = {
            CompositionDbResponse(
                isSuccess = true,
                data = MsCompositionDiscussion(
                    composition = MsComposition(
                        id = MsCompositionId("test composition"),
                        owner = MsUserId("test user"),
                        file = File("owner-123"),
                    )
                )
            )
        }
    )

    private val settings = MsSettings(
        repoTest = repo
    )
    private val processor = MsCompositionProcessor(settings = settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MsContext(
            command = command,
            state = MsState.NONE,
            workMode = MsWorkMode.TEST,
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(
                    owner = MsUserId("test user"),
                    file = File("owner-123"),
                )
            ),
            principal = MsPrincipalModel(
                id = MsUserId("test user"),
                groups = setOf(
                    MsUserGroups.USER,
                    MsUserGroups.TEST,
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(MsState.FINISHING, ctx.state)
        assertNotEquals(MsCompositionId.NONE, ctx.compositionResponse.composition.id)
        assertEquals(MsUserId("test user"), ctx.compositionResponse.composition.owner)
        assertEquals(File("owner-123"), ctx.compositionResponse.composition.file)
    }
}