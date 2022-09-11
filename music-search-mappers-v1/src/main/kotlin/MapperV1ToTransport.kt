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
import ru.otus.music.search.api.v1.models.Error
import ru.otus.music.search.api.v1.models.IResponse
import ru.otus.music.search.api.v1.models.ResponseResult
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.mappers.v1.exceptions.UnknownMsCommand

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

private fun MsComposition.toCompositionInfo() =
    CompositionInfo(
        id = id.asString().takeIfNotBlank(),
        composition = BaseComposition(
            file = file,
            owner = owner.asString()
        )
    )

private fun MsContext.toTransportCreate() =
    CompositionCreateResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        compositionInfo = compositionResponse.composition.toCompositionInfo()
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
        comment = toTransportBaseComment()
    )

private fun Set<MsComment>.toTransportComments() =
    this.map { it.toTransportCommentInfo() }
        .toList()

private fun MsContext.toTransportRead() =
    CompositionReadResponse(
        requestId = requestId.asString().takeIfNotBlank(),
        result = if (state == MsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR, // зачем такое решение
        errors = errors.toTransportErrors(),
        compositionInfo = compositionResponse.composition.toCompositionInfo(),
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
        compositionInfo = compositionResponse.composition.toCompositionInfo(),
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
    this.map { it.composition.toCompositionInfo() }
        .toList()

// почему множество ошибок
private fun List<MsError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MsError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
