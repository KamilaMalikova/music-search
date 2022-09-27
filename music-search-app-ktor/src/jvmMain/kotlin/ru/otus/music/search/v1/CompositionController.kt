package ru.otus.music.search.v1

import io.ktor.server.application.*
import ru.otus.music.search.api.v1.models.CommentAcceptRequest
import ru.otus.music.search.api.v1.models.CommentAcceptResponse
import ru.otus.music.search.api.v1.models.CommentAddRequest
import ru.otus.music.search.api.v1.models.CommentAddResponse
import ru.otus.music.search.api.v1.models.CommentDeclineRequest
import ru.otus.music.search.api.v1.models.CommentDeclineResponse
import ru.otus.music.search.api.v1.models.CompositionCreateRequest
import ru.otus.music.search.api.v1.models.CompositionCreateResponse
import ru.otus.music.search.api.v1.models.CompositionReadRequest
import ru.otus.music.search.api.v1.models.CompositionReadResponse
import ru.otus.music.search.api.v1.models.CompositionSearchRequest
import ru.otus.music.search.api.v1.models.CompositionSearchResponse
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.models.MsCommand

suspend fun ApplicationCall.createComposition(processor: MsCompositionProcessor) =
    processV1<CompositionCreateRequest, CompositionCreateResponse>(processor, MsCommand.CREATE)

suspend fun ApplicationCall.readCompositionDiscussion(processor: MsCompositionProcessor) =
    processV1<CompositionReadRequest, CompositionReadResponse>(processor, MsCommand.READ)

suspend fun ApplicationCall.searchCompositionDiscussion(processor: MsCompositionProcessor) =
    processV1<CompositionSearchRequest, CompositionSearchResponse>(processor, MsCommand.SEARCH)

suspend fun ApplicationCall.commentComposition(processor: MsCompositionProcessor) =
    processV1<CommentAddRequest, CommentAddResponse>(processor, MsCommand.COMMENT)

suspend fun ApplicationCall.acceptComment(processor: MsCompositionProcessor) =
    processV1<CommentAcceptRequest, CommentAcceptResponse>(processor, MsCommand.ACCEPT)

suspend fun ApplicationCall.declineComment(processor: MsCompositionProcessor) =
    processV1<CommentDeclineRequest, CommentDeclineResponse>(processor, MsCommand.DECLINE)
