package ru.otus.music.search.repo.cassandra

import com.benasher44.uuid.uuid4
import java.util.concurrent.CompletionStage
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import ru.otus.music.search.common.helpers.asMsError
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.repo.CommentDbRequest
import ru.otus.music.search.common.repo.CommentIdDbRequest
import ru.otus.music.search.common.repo.CommentUpdateDbRequest
import ru.otus.music.search.common.repo.CommentsFilterDbRequest
import ru.otus.music.search.common.repo.CommetnsFilterDbResponse
import ru.otus.music.search.common.repo.CompositionDbResponse
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest
import ru.otus.music.search.common.repo.CompositionFilterDbRequest
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.CompositionIdDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository
import ru.otus.music.search.repo.cassandra.model.CommentCassandraDto
import ru.otus.music.search.repo.cassandra.model.DiscussionCassandraDto

class RepoCassandra(
    private val compositionDao: CompositionCassandraDao,
    private val commentDao: CommentsCassandraDao,
    private val timeoutMillis: Long = 300_000,
    private val randomUuid: () -> String = { uuid4().toString() }
): ICompositionRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private fun errorToResponse(e: Exception) = CompositionDbResponse.error(e.asMsError())
    private fun errorToFilterResponse(e: Exception) = CompositionFilterDbResponse.error(e.asMsError())
    private fun errorToCommentFilterResponse(e: Exception) = CommetnsFilterDbResponse.error(e.asMsError())

    private suspend inline fun readAndDoDbAction(
        name: String,
        id: MsCompositionId,
        successResult: MsCompositionDiscussion?,
        daoAction: () -> CompletionStage<Boolean>,
        errorToResponse: (Exception) -> CompositionDbResponse
    ): CompositionDbResponse =
        if (id == MsCompositionId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            name,
            {
                val read = compositionDao.read(id.asString()).await()
                if (read == null) ID_NOT_FOUND
                else {
                    val success = daoAction().await()
                    if (success) CompositionDbResponse.success(successResult ?: read.toModel())
                    else CompositionDbResponse(
                        read.toModel(),
                        false,
                        CONCURRENT_MODIFICATION.errors
                    )
                }
            },
            errorToResponse
        )

    private suspend inline fun readAndDoCommentDbAction(
        name: String,
        id: MsCompositionId,
        commentId: MsCommentId,
        successResult: MsCompositionDiscussion?,
        daoAction: () -> CompletionStage<Boolean>,
        errorToResponse: (Exception) -> CompositionDbResponse
    ): CompositionDbResponse =
        if (id == MsCompositionId.NONE || commentId == MsCommentId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            name,
            {
                val read = compositionDao.read(id.asString()).await()
                if (read == null) ID_NOT_FOUND
                else {
                    val commentRead = commentDao.read(id.asString(), commentId.asString()).await()
                    if (commentRead == null) ID_NOT_FOUND
                    else {
                        val success = daoAction().await()
                        if (success) CompositionDbResponse.success(successResult ?: read.toModel())
                        else CompositionDbResponse(
                            read.toModel().copy(comment = commentRead.toModel()),
                            false,
                            CONCURRENT_MODIFICATION.errors
                        )
                    }

                }
            },
            errorToResponse
        )

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeoutMillis) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse
    )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse {
        val newComposition = rq.discussion.composition.copy(id = MsCompositionId(randomUuid()))
        val new = rq.discussion.copy(composition = newComposition, lock = MsCompositionLock(randomUuid()))

        return doDbAction(
            "create composition",
            { compositionDao.create(DiscussionCassandraDto(new)) },
            { CompositionDbResponse.success(new) },
            ::errorToResponse
        )
    }

    override suspend fun readComposition(rq: CompositionIdDbRequest): CompositionDbResponse =
        if (rq.id == MsCompositionId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            "read",
            { compositionDao.read(rq.id.asString()) },
            { found ->
                if (found != null) {
                    val comments = searchComments(rq.id)

                    if (comments.isSuccess) {
                        CompositionDbResponse.success(found.toModel().copy(comments = comments.data?.toMutableSet() ?: mutableSetOf()))
                    } else {
                        CompositionDbResponse.error(comments.errors)
                    }

                }
                else ID_NOT_FOUND
            },
            ::errorToResponse
        )

    private suspend fun searchComments(compositionId: MsCompositionId): CommetnsFilterDbResponse =
        doDbAction(
        "search-comments",
        { commentDao.search(CommentsFilterDbRequest(compositionId)) },
        { found -> CommetnsFilterDbResponse.success(found.map { it.toModel() }) },
        ::errorToCommentFilterResponse
    )

    override suspend fun deleteComposition(rq: CompositionIdDbRequest): CompositionDbResponse =
        readAndDoDbAction(
            "delete",
            rq.id,
            null,
            {
//                commentDao.deleteAll(rq.id.asString(), rq.lock.asString())
                compositionDao.delete(rq.id.asString(), rq.lock.asString())
            },
            ::errorToResponse
        )

    override suspend fun updateComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse {
        val prevLock = rq.discussion.lock.asString()
        val new = rq.discussion.copy(lock = MsCompositionLock(randomUuid()))
        val dto = DiscussionCassandraDto(new)

        return readAndDoDbAction(
            "update",
            rq.discussion.composition.id,
            new,
            { compositionDao.update(dto, prevLock) },
            ::errorToResponse
        )
    }

    override suspend fun createComment(rq: CommentDbRequest): CompositionDbResponse {
        if (rq.compositionId == MsCompositionId.NONE)
            ID_IS_EMPTY
        val newComment= rq.comment.copy(id = MsCommentId(randomUuid()), lock = MsCompositionLock(randomUuid()))
        val read = compositionDao.read(rq.compositionId.asString()).await()
        return if (read == null) ID_NOT_FOUND
        else doDbAction(
            "create comment",
            { commentDao.create(CommentCassandraDto(rq.compositionId, newComment)) },
            {
                val discussion = MsCompositionDiscussion(
                    composition = MsComposition(id = rq.compositionId),
                    comment = newComment
                )
                CompositionDbResponse.success(discussion)
            },
            ::errorToResponse
        )
    }

    override suspend fun updateComment(rq: CommentUpdateDbRequest): CompositionDbResponse {
        val prevLock = rq.comment.lock.asString()

        val dto = CommentCassandraDto(rq.compositionId, rq.comment.copy(lock = MsCompositionLock(randomUuid())))

        return readAndDoCommentDbAction(
            "update comment",
            rq.compositionId,
            rq.comment.id,
            MsCompositionDiscussion(
                composition = MsComposition( id = rq.compositionId),
                comment = rq.comment
            ),
            { commentDao.update(dto, prevLock) },
            ::errorToResponse
        )
    }

    override suspend fun deleteComment(rq: CommentIdDbRequest): CompositionDbResponse =
        readAndDoCommentDbAction(
            "delete comment",
            rq.compositionId,
            rq.commentId,
            null,
            {
                commentDao.delete(rq.compositionId.asString(), rq.commentId.asString(), rq.lock.asString())
            },
            ::errorToResponse
        )

    override suspend fun filter(rq: CompositionFilterDbRequest): CompositionFilterDbResponse =
        doDbAction(
            "search",
            { compositionDao.search(rq) },
            { found ->
                CompositionFilterDbResponse.success(found.map { it.toModel() })
            },
            ::errorToFilterResponse
        )

    companion object {
        private val ID_IS_EMPTY = CompositionDbResponse.error(MsError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND =
            CompositionDbResponse.error(MsError(field = "id", code = "not-found", message = "Not Found"))
        private val CONCURRENT_MODIFICATION =
            CompositionDbResponse.error(MsError(field = "lock", code = "concurrency", message = "Concurrent modification"))
    }
}