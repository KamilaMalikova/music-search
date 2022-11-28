package ru.otus.music.search.common.repo

interface ICompositionRepository {
    suspend fun createComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse

    suspend fun readComposition(rq: CompositionIdDbRequest): CompositionDbResponse

    suspend fun deleteComposition(rq: CompositionIdDbRequest): CompositionDbResponse

    suspend fun updateComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse

    suspend fun readComment(rq: CommentIdDbRequest): CompositionDbResponse

    suspend fun createComment(rq: CommentDbRequest): CompositionDbResponse

    suspend fun updateComment(rq: CommentUpdateDbRequest): CompositionDbResponse

    suspend fun deleteComment(rq: CommentIdDbRequest): CompositionDbResponse

    suspend fun filter(rq: CompositionFilterRequest): CompositionFilterDbResponse

    companion object {
        val NONE = object : ICompositionRepository {
            override suspend fun createComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readComposition(rq: CompositionIdDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteComposition(rq: CompositionIdDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readComment(rq: CommentIdDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun createComment(rq: CommentDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateComment(rq: CommentUpdateDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteComment(rq: CommentIdDbRequest): CompositionDbResponse {
                TODO("Not yet implemented")
            }

            override suspend fun filter(rq: CompositionFilterRequest): CompositionFilterDbResponse {
                TODO("Not yet implemented")
            }

        }
    }
}