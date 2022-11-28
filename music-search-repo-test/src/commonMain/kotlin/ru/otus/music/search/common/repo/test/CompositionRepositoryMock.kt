package ru.otus.music.search.common.repo.test

import ru.otus.music.search.common.repo.CommentDbRequest
import ru.otus.music.search.common.repo.CommentIdDbRequest
import ru.otus.music.search.common.repo.CommentUpdateDbRequest
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest
import ru.otus.music.search.common.repo.CompositionDbResponse
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.CompositionFilterRequest
import ru.otus.music.search.common.repo.CompositionIdDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository

class CompositionRepositoryMock(
    private val invokeCreateComposition: (CompositionDiscussionDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadComposition: (CompositionIdDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateComposition: (CompositionDiscussionDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteComposition: (CompositionIdDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeCreateComment: (CommentDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadComment: (CommentIdDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateComment: (CommentUpdateDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteComment: (CommentIdDbRequest) -> CompositionDbResponse = { CompositionDbResponse.MOCK_SUCCESS_EMPTY },
    private val invokeFilter: (CompositionFilterRequest) -> CompositionFilterDbResponse = { CompositionFilterDbResponse.MOCK_SUCCESS_FILTER_EMPTY },
): ICompositionRepository {
    override suspend fun createComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse =
        invokeCreateComposition(rq)


    override suspend fun readComposition(rq: CompositionIdDbRequest): CompositionDbResponse =
        invokeReadComposition(rq)

    override suspend fun deleteComposition(rq: CompositionIdDbRequest): CompositionDbResponse =
        invokeDeleteComposition(rq)

    override suspend fun updateComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse =
        invokeUpdateComposition(rq)

    override suspend fun readComment(rq: CommentIdDbRequest): CompositionDbResponse =
        invokeReadComment(rq)

    override suspend fun createComment(rq: CommentDbRequest): CompositionDbResponse =
        invokeCreateComment(rq)

    override suspend fun updateComment(rq: CommentUpdateDbRequest): CompositionDbResponse =
        invokeUpdateComment(rq)

    override suspend fun deleteComment(rq: CommentIdDbRequest): CompositionDbResponse =
        invokeDeleteComment(rq)

    override suspend fun filter(rq: CompositionFilterRequest): CompositionFilterDbResponse =
        invokeFilter(rq)
}