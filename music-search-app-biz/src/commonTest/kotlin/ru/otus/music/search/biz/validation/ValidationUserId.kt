package ru.otus.music.search.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsFile
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsUserGroups
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validateCorrectOwnerId(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                owner = MsUserId("12345"),
                file = MsFile.NONE
            )
        ),
        principal = MsPrincipalModel(
            id = MsUserId("12345"),
            groups = setOf(
                MsUserGroups.USER,
                MsUserGroups.TEST,
            )
        )
    )

    processor.exec(ctx)
    assertNotEquals(MsState.FAILING, ctx.state)
    assertEquals(MsUserId("12345"), ctx.msValidated.composition.owner)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateEmptyOwnerId(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                file = MsFile.NONE
            )
        )
    )

    processor.exec(ctx)
    assertEquals(MsState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)

    val error = ctx.errors.firstOrNull()
    assertEquals("owner", error?.field)
    assertContains(error?.code ?: "", "empty")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateCorrectAuthorIdNotEmpty(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val stub = MsCompositionDiscussionStub.get()
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = stub.composition.id,
                owner = stub.composition.owner,
                file = MsFile.NONE
            ),
            comment = MsComment(
                author = MsUserId("12345"),
                text = "Bang bang"
            )
        ),
        principal = MsPrincipalModel(
            id = MsUserId("12345"),
            groups = setOf(
                MsUserGroups.USER,
                MsUserGroups.TEST,
            )
        )
    )

    processor.exec(ctx)
    assertNotEquals(MsState.FAILING, ctx.state)
    assertEquals(MsUserId("12345"), ctx.msValidated.comment.author)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateIncorrectEmptyAuthorId(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = MsCompositionId("1234"),
                owner = MsUserId("12345"),
                file = MsFile.NONE
            ),
            comment = MsComment(
                id = MsCommentId("12345"),
                text = "Bang bang"
            )
        )
    )

    processor.exec(ctx)
    assertEquals(MsState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)

    val error = ctx.errors.firstOrNull()
    assertEquals("comment.author", error?.field)
    assertContains(error?.code ?: "", "empty")
}
