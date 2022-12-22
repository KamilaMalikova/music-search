package ru.otus.music.search.mappers.v1

import ru.otus.music.search.api.v1.models.BaseComment
import ru.otus.music.search.api.v1.models.CommentAcceptObject
import ru.otus.music.search.api.v1.models.CommentAcceptRequest
import ru.otus.music.search.api.v1.models.CommentAddObject
import ru.otus.music.search.api.v1.models.CommentAddRequest
import ru.otus.music.search.api.v1.models.CommentDeclineObject
import ru.otus.music.search.api.v1.models.CommentDeclineRequest
import ru.otus.music.search.api.v1.models.CommentStatus
import ru.otus.music.search.api.v1.models.CompositionCreateObject
import ru.otus.music.search.api.v1.models.CompositionCreateRequest
import ru.otus.music.search.api.v1.models.CompositionDebug
import ru.otus.music.search.api.v1.models.CompositionReadObject
import ru.otus.music.search.api.v1.models.CompositionReadRequest
import ru.otus.music.search.api.v1.models.CompositionRequestDebugMode
import ru.otus.music.search.api.v1.models.CompositionRequestDebugStubs
import ru.otus.music.search.api.v1.models.CompositionSearchFilter
import ru.otus.music.search.api.v1.models.CompositionSearchRequest
import ru.otus.music.search.api.v1.models.DiscussionStatus
import ru.otus.music.search.api.v1.models.IRequest
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsFile
import ru.otus.music.search.common.models.MsFilter
import ru.otus.music.search.common.models.MsRequestId
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.common.stubs.MsStub
import ru.otus.music.search.mappers.v1.exceptions.UnknownRequestClass

fun MsContext.fromTransport(request: IRequest) =
    when(request) {
        is CompositionCreateRequest -> fromTransport(request)
        is CompositionReadRequest -> fromTransport(request)
        is CommentAddRequest -> fromTransport(request)
        is CommentAcceptRequest -> fromTransport(request)
        is CommentDeclineRequest -> fromTransport(request)
        is CompositionSearchRequest -> fromTransport(request)
        else -> throw UnknownRequestClass(request.javaClass)
    }

private fun IRequest?.requestId() =
    this?.requestId?.let { MsRequestId(it) } ?: MsRequestId.NONE

private fun String?.toUserId() =
    this?.let { MsUserId(it) } ?: MsUserId.NONE

private fun String?.toMsFile() =
    this?.let { MsFile(it) } ?: MsFile.NONE

private fun String?.toCompositionId() =
    this?.let { MsCompositionId(it) } ?: MsCompositionId.NONE

private fun String?.toCommentId() =
    this?.let { MsCommentId(it) } ?: MsCommentId.NONE

private fun String?.toLock() =
    this?.let { MsCompositionLock(it) } ?: MsCompositionLock.NONE

private fun CompositionDebug?.transportToWorkMode(): MsWorkMode =
    when(this?.mode) {
        CompositionRequestDebugMode.PROD -> MsWorkMode.PROD
        CompositionRequestDebugMode.TEST -> MsWorkMode.TEST
        CompositionRequestDebugMode.STUB -> MsWorkMode.STUB
        null -> MsWorkMode.PROD
    }

private fun CompositionDebug?.transportToStubCase(): MsStub = when(this?.stub) {
    CompositionRequestDebugStubs.SUCCESS -> MsStub.SUCCESS
    CompositionRequestDebugStubs.NOT_FOUND -> MsStub.NOT_FOUND
    CompositionRequestDebugStubs.BAD_ID -> MsStub.BAD_ID
    CompositionRequestDebugStubs.DB_ERROR -> MsStub.DB_ERROR
    CompositionRequestDebugStubs.BAD_TEXT -> MsStub.BAD_TEXT
    CompositionRequestDebugStubs.CANNOT_DELETE -> MsStub.CANNOT_DELETE
    CompositionRequestDebugStubs.BAD_SEARCH_STRING -> MsStub.BAD_SEARCH_STRING
    null -> MsStub.NONE
}

private fun CompositionCreateObject.toInternal() =
    MsCompositionDiscussion(
        composition = MsComposition(
            file = file.toMsFile(),
            owner = owner.toUserId()
        )
    )

fun MsContext.fromTransport(request: CompositionCreateRequest) {
    command = MsCommand.CREATE
    requestId = request.requestId()
    msRequest = request.composition?.toInternal() ?: MsCompositionDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

}

private fun CompositionReadObject.toInternal() =
    MsCompositionDiscussion(
        composition = MsComposition(id = id.toCompositionId())
    )

fun MsContext.fromTransport(request: CompositionReadRequest) {
    command = MsCommand.READ
    requestId = request.requestId()
    msRequest = request.composition?.toInternal() ?: MsCompositionDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()

}

private fun CommentStatus?.transportToMsStatus(): MsCommentStatus =
    when(this) {
        CommentStatus.NONE -> MsCommentStatus.NONE
        CommentStatus.ACCEPTED -> MsCommentStatus.ACCEPTED
        CommentStatus.DECLINED -> MsCommentStatus.DECLINED
        null -> MsCommentStatus.NONE
    }

private fun BaseComment.toMsComment() =
    MsComment(
        author = author.toUserId(),
        text = text ?: "",
        status = status.transportToMsStatus()
    )

private fun CommentAddObject.toInternal() =
    MsCompositionDiscussion(
        composition = MsComposition(id = compositionId.toCompositionId()),
        comment = comment?.toMsComment() ?: MsComment(),
        lock = lock.toLock()
    )

fun MsContext.fromTransport(request: CommentAddRequest) {
    command = MsCommand.COMMENT
    requestId = request.requestId()
    msRequest = request.composition?.toInternal() ?: MsCompositionDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun CommentAcceptObject.toInternal() =
    MsCompositionDiscussion(
        composition = MsComposition(id = compositionId.toCompositionId()),
        comment = MsComment(id = commentId.toCommentId(), lock = commentLock.toLock()),
        lock = lock.toLock()
    )

fun MsContext.fromTransport(request: CommentAcceptRequest) {
    command = MsCommand.ACCEPT
    requestId = request.requestId()
    msRequest = request.composition?.toInternal() ?: MsCompositionDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun CommentDeclineObject.toInternal() =
    MsCompositionDiscussion(
        composition = MsComposition(id = compositionId.toCompositionId()),
        comment = MsComment(id = commentId.toCommentId(), lock = commentLock.toLock()),
        lock = lock.toLock()
    )

fun MsContext.fromTransport(request: CommentDeclineRequest) {
    command = MsCommand.DECLINE
    requestId = request.requestId()
    msRequest = request.composition?.toInternal() ?: MsCompositionDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DiscussionStatus?.transportToMsStatus(): MsDiscussionStatus =
    when(this) {
        DiscussionStatus.OPEN -> MsDiscussionStatus.OPEN
        DiscussionStatus.CLOSED -> MsDiscussionStatus.CLOSED
        null -> MsDiscussionStatus.NONE
    }

private fun CompositionSearchFilter.toInternal() =
    MsFilter(
        discussionStatus = discussionStatus.transportToMsStatus(),
        ownerId = owner.toUserId()
    )

fun MsContext.fromTransport(request: CompositionSearchRequest) {
    command = MsCommand.SEARCH
    requestId = request.requestId()
    filterRequest = request.filter?.toInternal() ?: MsFilter()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}