package ru.otus.music.search.mappers.v1

import ru.otus.music.search.api.v1.models.BaseComment
import ru.otus.music.search.api.v1.models.BaseComposition
import ru.otus.music.search.api.v1.models.CommentAcceptResponse
import ru.otus.music.search.api.v1.models.CommentAddResponse
import ru.otus.music.search.api.v1.models.CommentDeclineResponse
import ru.otus.music.search.api.v1.models.CommentInfo
import ru.otus.music.search.api.v1.models.CommentStatus
import ru.otus.music.search.api.v1.models.CompositionCreateResponse
import ru.otus.music.search.api.v1.models.CompositionInfo
import ru.otus.music.search.api.v1.models.CompositionReadResponse
import ru.otus.music.search.api.v1.models.CompositionSearchResponse
import ru.otus.music.search.api.v1.models.DiscussionStatus
import ru.otus.music.search.api.v1.models.Error
import ru.otus.music.search.api.v1.models.IResponse
import ru.otus.music.search.api.v1.models.ResponseResult
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.mappers.v1.exceptions.UnknownMsCommand
import ru.otus.music.search.mappers.v1.exceptions.UnknownMsStatus

fun MsContext.toTransport(): IResponse =
    when(command) {
        MsCommand.CREATE -> toTransportCreate()
        MsCommand.READ -> toTransportRead()
        MsCommand.COMMENT -> toTransportComment()
        MsCommand.ACCEPT-> toTransportAccept()
        MsCommand.DECLINE -> toTransportDecline()
        MsCommand.SEARCH -> toTransportSearch()
        MsCommand.NONE -> throw UnknownMsCommand(command)
    }

private fun String.takeIfNotBlank() =
    this.takeIf { it.isNotBlank() }

private fun MsCompositionDiscussion.toCompositionInfo() =
    CompositionInfo(
        id = composition.id.asString().takeIfNotBlank(),
        composition = BaseComposition(
            file = composition.file.asString(),
            owner = composition.owner.asString(),
            status = status.toTransport()
        ),
        lock = lock.asString()
    )

private fun MsDiscussionStatus.toTransport() =
    when(this) {
        MsDiscussionStatus.OPEN -> DiscussionStatus.OPEN
        MsDiscussionStatus.CLOSED -> DiscussionStatus.CLOSED
        MsDiscussionStatus.NONE -> throw UnknownMsStatus(this.name)
    }

private fun MsContext.toTransportCreate() =
    CompositionCreateResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        compositionInfo = compositionResponse.toCompositionInfo()
    )

private fun MsCommentStatus.toTransportCommentStatus() =
    when(this) {
        MsCommentStatus.ACCEPTED -> CommentStatus.ACCEPTED
        MsCommentStatus.DECLINED -> CommentStatus.DECLINED
        MsCommentStatus.NONE -> CommentStatus.NONE
    }

private fun MsComment.toTransportBaseComment() =
    BaseComment(
        author = author.asString().takeIfNotBlank(),
        text = text,
        status = status.toTransportCommentStatus()
    )

private fun MsComment.toTransportCommentInfo() =
    CommentInfo(
        id = id.asString().takeIfNotBlank(),
        comment = toTransportBaseComment(),
        commentLock = lock.asString().takeIfNotBlank()
    )

private fun Set<MsComment>.toTransportComments() =
    this.map { it.toTransportCommentInfo() }
        .toList()

private fun MsContext.toTransportRead() =
    CompositionReadResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        compositionInfo = compositionResponse.toCompositionInfo(),
        comments = compositionResponse.comments.toTransportComments()
    )

private fun MsContext.toTransportComment() =
    CommentAddResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        commentInfo = compositionResponse.comment.toTransportCommentInfo()
    )

private fun MsContext.toTransportAccept() =
    CommentAcceptResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        compositionInfo = compositionResponse.toCompositionInfo(),
        comments = compositionResponse.comments.toTransportComments()
    )

private fun MsContext.toTransportDecline() =
    CommentDeclineResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        commentInfo = compositionResponse.comment.toTransportCommentInfo()
    )

private fun MsContext.toTransportSearch() =
    CompositionSearchResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        compositions = compositionsResponse.toTransportCompositions()
    )

private fun List<MsCompositionDiscussion>.toTransportCompositions() =
    this.map { it.toCompositionInfo() }
        .toList()

private fun List<MsError>.toTransportErrors(): List<Error>? =
    this.map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MsError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
