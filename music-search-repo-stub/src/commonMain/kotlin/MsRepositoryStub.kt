import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.repo.CommentDbRequest
import ru.otus.music.search.common.repo.CommentIdDbRequest
import ru.otus.music.search.common.repo.CommentUpdateDbRequest
import ru.otus.music.search.common.repo.CompositionDbResponse
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.CompositionFilterRequest
import ru.otus.music.search.common.repo.CompositionIdDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository

class MsRepositoryStub(): ICompositionRepository {
    override suspend fun createComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun readComposition(rq: CompositionIdDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun deleteComposition(rq: CompositionIdDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun updateComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun readComment(rq: CommentIdDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun createComment(rq: CommentDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun updateComment(rq: CommentUpdateDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun deleteComment(rq: CommentIdDbRequest): CompositionDbResponse =
        CompositionDbResponse(
            data = MsCompositionDiscussionStub.prepareResult {  },
            isSuccess = true
        )

    override suspend fun filter(rq: CompositionFilterRequest): CompositionFilterDbResponse =
        CompositionFilterDbResponse(
            data = MsCompositionDiscussionStub.prepareSearchList(
                status = MsDiscussionStatus.OPEN,
                owner = MsUserId("123")
            ),
            isSuccess = true
        )
}