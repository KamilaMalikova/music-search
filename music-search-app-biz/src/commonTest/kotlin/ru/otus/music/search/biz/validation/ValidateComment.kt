package ru.otus.music.search.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.EMPTY_FILE
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.permissions.MsPrincipalModel
import ru.otus.music.search.common.permissions.MsUserGroups
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validateCorrectComment(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = MsCompositionId("1234"),
                owner = MsUserId("12345"),
                file = EMPTY_FILE
            ),
            comment = MsComment(
                author = MsUserId("1234"),
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
    assertEquals("Bang bang", ctx.msValidated.comment.text)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateTrimComment(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = MsCompositionId("1234"),
                owner = MsUserId("12345"),
                file = EMPTY_FILE
            ),
            comment = MsComment(
                author = MsUserId("12345"),
                text = "\n\tBang bang"
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
    assertEquals("Bang bang", ctx.msValidated.comment.text)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateIncorrectEmptyComment(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = MsCompositionId("1234"),
                owner = MsUserId("12345"),
                file = EMPTY_FILE
            ),
            comment = MsComment(
                author = MsUserId("1234")
            )
        )
    )

    processor.exec(ctx)

    assertEquals(MsState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)

    val error = ctx.errors.firstOrNull()
    assertEquals("comment.text", error?.field)
    assertContains(error?.code ?: "", "empty")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateIncorrectNoContentInComment(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = MsCompositionId("1234"),
                owner = MsUserId("12345"),
                file = EMPTY_FILE
            ),
            comment = MsComment(
                author = MsUserId("1234"),
                text = "!@#4%%^&*()_"
            )
        )
    )

    processor.exec(ctx)

    assertEquals(MsState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)

    val error = ctx.errors.firstOrNull()
    assertEquals("comment.text", error?.field)
    assertContains(error?.code ?: "", "no-content")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateCorrectCommentIdNotEmpty(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val stub = MsCompositionDiscussionStub.get()
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = stub.composition.id
            ),
            comment = MsComment(
                id = stub.comments.firstOrNull()?.id ?: MsCommentId.NONE
            )
        ),
        principal = MsPrincipalModel(
            id = stub.composition.owner,
            groups = setOf(
                MsUserGroups.USER,
                MsUserGroups.TEST,
            )
        )
    )

    processor.exec(ctx)
    assertNotEquals(MsState.FAILING, ctx.state)
    assertEquals(stub.comments.firstOrNull()?.id, ctx.msValidated.comment.id)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validateIncorrectEmptyCommentId(command: MsCommand, processor: MsCompositionProcessor) = runTest {
    val ctx = MsContext(
        command = command,
        state = MsState.NONE,
        workMode = MsWorkMode.TEST,
        msRequest = MsCompositionDiscussion(
            composition = MsComposition(
                id = MsCompositionId("1234"),
                owner = MsUserId("12345"),
                file = EMPTY_FILE
            ),
            comment = MsComment()
        )
    )

    processor.exec(ctx)
    assertEquals(MsState.FAILING, ctx.state)
    assertEquals(1, ctx.errors.size)

    val error = ctx.errors.firstOrNull()
    assertEquals("comment.id", error?.field)
    assertContains(error?.code ?: "", "empty")
}
