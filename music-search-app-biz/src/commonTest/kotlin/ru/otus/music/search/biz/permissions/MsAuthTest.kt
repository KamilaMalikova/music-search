package ru.otus.music.search.biz.permissions

import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.otus.music.search.MsCompositionDiscussionStub
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
import ru.otus.music.search.common.permissions.MsPermissionClient
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsUserGroups
import ru.otus.music.search.common.repo.inmemory.CompositionRepoInMemory
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MsAuthTest {
    @Test
    fun repoCreateSuccessTest() = runTest {
        val userId = MsUserId("123")
        val repo = CompositionRepoInMemory()
        val processor = MsCompositionProcessor(
            settings = MsSettings(
                repoTest = repo
            )
        )
        val ctx = MsContext(
            command = MsCommand.CREATE,
            state = MsState.NONE,
            workMode = MsWorkMode.TEST,
            principal = MsPrincipalModel(
                id = userId,
                groups = setOf(
                    MsUserGroups.USER,
                    MsUserGroups.TEST,
                )
            ),
            msRequest = MsCompositionDiscussion(
                composition = MsComposition(
                    owner = userId,
                    file = File("owner-123"),
                )
            )
        )
        processor.exec(ctx)
        assertEquals(MsState.FINISHING, ctx.state)
        assertNotEquals(MsCompositionId.NONE, ctx.compositionResponse.composition.id)
        assertEquals(userId, ctx.compositionResponse.composition.owner)
        assertEquals(File("owner-123"), ctx.compositionResponse.composition.file)
        with(ctx.compositionResponse) {
            assertTrue { composition.id.asString().isNotBlank() }
            assertContains(permissionsClient, MsPermissionClient.READ)
            assertContains(permissionsClient, MsPermissionClient.UPDATE)
            assertContains(permissionsClient, MsPermissionClient.ADD_COMMENT)
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val msObj = MsCompositionDiscussionStub.get()
        val userId = msObj.composition.owner
        val compositionId = msObj.composition.id
        val repo = CompositionRepoInMemory(initObjects = listOf(msObj))
        val processor = MsCompositionProcessor(
            settings = MsSettings(
                repoTest = repo
            )
        )
        val context = MsContext(
            command = MsCommand.READ,
            workMode = MsWorkMode.TEST,
            msRequest = MsCompositionDiscussion(composition = MsComposition(id = compositionId)),
            principal = MsPrincipalModel(
                id = userId,
                groups = setOf(
                    MsUserGroups.USER,
                    MsUserGroups.TEST,
                )
            )
        )
        processor.exec(context)
        assertEquals(MsState.FINISHING, context.state)
        with(context.compositionResponse) {
            assertTrue { composition.id.asString().isNotBlank() }
            assertContains(permissionsClient, MsPermissionClient.READ)
            assertContains(permissionsClient, MsPermissionClient.UPDATE)
        }
    }
}