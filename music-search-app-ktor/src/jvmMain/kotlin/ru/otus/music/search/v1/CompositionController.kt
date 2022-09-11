package ru.otus.music.search.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.api.v1.models.CommentAcceptRequest
import ru.otus.music.search.api.v1.models.CommentAddRequest
import ru.otus.music.search.api.v1.models.CommentDeclineRequest
import ru.otus.music.search.api.v1.models.CompositionCreateRequest
import ru.otus.music.search.api.v1.models.CompositionReadRequest
import ru.otus.music.search.api.v1.models.CompositionSearchRequest
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.mappers.v1.fromTransport
import ru.otus.music.search.mappers.v1.toTransport

suspend fun ApplicationCall.createComposition() {
    val request = receive<CompositionCreateRequest>()
    val context = MsContext()

    context.fromTransport(request)
    context.compositionResponse = MsCompositionDiscussionStub.get()
    respond(context.toTransport())
}

suspend fun ApplicationCall.readCompositionDiscussion() {
    val request = receive<CompositionReadRequest>()
    val context = MsContext()

    context.fromTransport(request)
    context.compositionResponse = MsCompositionDiscussionStub.get()
    respond(context.toTransport())
}

suspend fun ApplicationCall.searchCompositionDiscussion() {
    val request = receive<CompositionSearchRequest>()
    val context = MsContext()

    context.fromTransport(request)
    context.compositionsResponse = MsCompositionDiscussionStub.prepareSearchList(
        MsDiscussionStatus.OPEN,
        MsUserId("123")
    )
    respond(context.toTransport())
}

suspend fun ApplicationCall.commentComposition() {
    val request = receive<CommentAddRequest>()
    val context = MsContext()

    context.fromTransport(request)
    context.compositionResponse = MsCompositionDiscussionStub.get()
    respond(context.toTransport())
}

suspend fun ApplicationCall.acceptComment() {
    val request = receive<CommentAcceptRequest>()
    val context = MsContext()

    context.fromTransport(request)
    context.compositionResponse = MsCompositionDiscussionStub.get()
    respond(context.toTransport())
}

suspend fun ApplicationCall.declineComment() {
    val request = receive<CommentDeclineRequest>()
    val context = MsContext()

    context.fromTransport(request)
    context.compositionResponse = MsCompositionDiscussionStub.get()
    respond(context.toTransport())
}
