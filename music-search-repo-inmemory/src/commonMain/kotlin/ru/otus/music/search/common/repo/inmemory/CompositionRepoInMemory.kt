package ru.otus.music.search.common.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.music.search.common.helpers.errorRepoConcurrency
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.repo.CommentDbRequest
import ru.otus.music.search.common.repo.CommentIdDbRequest
import ru.otus.music.search.common.repo.CommentUpdateDbRequest
import ru.otus.music.search.common.repo.CompositionDiscussionDbRequest
import ru.otus.music.search.common.repo.CompositionDbResponse
import ru.otus.music.search.common.repo.CompositionFilterDbResponse
import ru.otus.music.search.common.repo.CompositionFilterDbRequest
import ru.otus.music.search.common.repo.CompositionIdDbRequest
import ru.otus.music.search.common.repo.ICompositionRepository
import ru.otus.music.search.common.repo.inmemory.model.CompositionEntity

class CompositionRepoInMemory(
    initObjects: List<MsCompositionDiscussion> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
): ICompositionRepository {

    /**
     * Инициализация кеша с установкой "времени жизни" данных после записи
     */
    private val cache = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, CompositionEntity>()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(cd: MsCompositionDiscussion) {
        val entity = CompositionEntity(cd)
        entity.id?.let { cache.put(it, entity) }
    }

    override suspend fun createComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse {
        val key = randomUuid()
        val composition = rq.discussion.composition.copy(id = MsCompositionId(key))
        val discussion = rq.discussion.copy(composition = composition, lock = MsCompositionLock(randomUuid()))
        save(discussion)
        return CompositionDbResponse(
            data = discussion,
            isSuccess = true
        )
    }

    override suspend fun readComposition(rq: CompositionIdDbRequest): CompositionDbResponse {
        val key = rq.id.takeIfNotNone() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                CompositionDbResponse(
                    data = it.toInternal(),
                    isSuccess = true
                )
            } ?: resultErrorNotFound
    }

    override suspend fun deleteComposition(rq: CompositionIdDbRequest): CompositionDbResponse {
        val key = rq.id.takeIfNotNone() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIfNotNone() ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldComposition = cache.get(key)
            when {
                oldComposition == null -> resultErrorNotFound
                oldComposition.lock != oldLock -> CompositionDbResponse(
                    data = oldComposition.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            MsCompositionLock(oldLock),
                            oldComposition.lock?.let { MsCompositionLock(it) }
                        )
                    )
                )
                else -> {
                    cache.invalidate(key)
                    CompositionDbResponse(
                        data = oldComposition.toInternal(),
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun updateComposition(rq: CompositionDiscussionDbRequest): CompositionDbResponse {
        val key = rq.discussion.composition.id.takeIfNotNone() ?: return resultErrorEmptyId
        val oldLock = rq.discussion.lock.takeIfNotNone() ?: return resultErrorEmptyLock

        val newComposition = rq.discussion.copy(lock = MsCompositionLock(randomUuid()))
        val entity = CompositionEntity(newComposition)

        return mutex.withLock {
            val oldComposition = cache.get(key)
            when {
                oldComposition == null -> resultErrorNotFound
                oldComposition.lock != oldLock -> CompositionDbResponse(
                    data = oldComposition.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            MsCompositionLock(oldLock),
                            oldComposition.lock?.let { MsCompositionLock(it) }
                        )
                    )
                )
                else -> {
                    cache.put(key, entity)
                    CompositionDbResponse(
                        data = newComposition,
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun createComment(rq: CommentDbRequest): CompositionDbResponse {
        val key = rq.compositionId.takeIfNotNone() ?: return resultErrorEmptyId
        val commentId = MsCommentId(randomUuid())
        val oldLock = rq.lock.takeIfNotNone() ?: return resultErrorEmptyLock

        return mutex.withLock{
            val oldDiscussion = cache.get(key)
            when {
                oldDiscussion == null -> resultErrorNotFound
                oldDiscussion.lock != oldLock -> CompositionDbResponse(
                    data = oldDiscussion.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            MsCompositionLock(oldLock),
                             oldDiscussion.lock?.let { MsCompositionLock(it) }
                        )
                    )
                )
                else -> {
                    val newComment = rq.comment.copy(id = commentId, lock = MsCompositionLock(randomUuid()))
                    val newDiscussion = oldDiscussion.toInternal().copy(lock = MsCompositionLock(randomUuid()))
                        .apply {
                            comments.add(newComment)
                        }
                    val entity = CompositionEntity(newDiscussion)
                    cache.put(key, entity)
                    CompositionDbResponse(
                        data = newDiscussion.copy(comment = newComment),
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun updateComment(rq: CommentUpdateDbRequest): CompositionDbResponse {
        val key = rq.compositionId.takeIfNotNone() ?: return resultErrorEmptyId
        val commentId = rq.comment.id.takeIf { it != MsCommentId.NONE } ?: return resultErrorEmptyCommentId
        val discussionLock = rq.lock.asString()
        val newComment = rq.comment.copy(lock = MsCompositionLock(randomUuid()))
        return mutex.withLock {
            val oldDiscussion = cache.get(key)
            when {
                oldDiscussion == null -> resultErrorNotFound
                oldDiscussion.lock != discussionLock -> CompositionDbResponse(
                    data = oldDiscussion.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            MsCompositionLock(discussionLock),
                            oldDiscussion.lock?.let { MsCompositionLock(it) }
                        )
                    )
                )
                else -> {
                    val newDiscussion = oldDiscussion.toInternal().copy(lock = MsCompositionLock(randomUuid()))
                        .apply {
                            comments.firstOrNull { it.id == commentId }
                                ?.also {
                                    comments.remove(it)
                                } ?: return resultErrorCommentNotFound
                            comments.add(newComment)
                        }
                    val entity = CompositionEntity(newDiscussion)
                    cache.put(key, entity)
                    CompositionDbResponse(
                        data = newDiscussion,
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun deleteComment(rq: CommentIdDbRequest): CompositionDbResponse {
        val key = rq.compositionId.takeIfNotNone() ?: return resultErrorEmptyId
        val commentId = rq.commentId.takeIf { it != MsCommentId.NONE } ?: return resultErrorEmptyCommentId
        val oldLock = rq.lock.takeIfNotNone() ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldDiscussion = cache.get(key)
            when {
                oldDiscussion == null -> resultErrorNotFound
                oldDiscussion.lock != oldLock -> CompositionDbResponse(
                    data = oldDiscussion.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            MsCompositionLock(oldLock),
                            oldDiscussion.lock?.let { MsCompositionLock(it) }
                        )
                    )
                )
                else -> {
                    val newDiscussion = oldDiscussion.toInternal().copy(lock = MsCompositionLock(randomUuid()))
                        .apply {
                            comments.firstOrNull { it.id == commentId }
                                ?.also {
                                    comments.remove(it)
                                } ?: return resultErrorCommentNotFound
                        }
                    val entity = CompositionEntity(newDiscussion)
                    cache.put(key, entity)
                    CompositionDbResponse(
                        data = newDiscussion,
                        isSuccess = true
                    )
                }
            }
        }
    }

    override suspend fun filter(rq: CompositionFilterDbRequest): CompositionFilterDbResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MsUserId.NONE }?.let {
                    it.asString() == entry.value.owner
                } ?: true
            }
            .filter { entry ->
                rq.status.takeIf { it != MsDiscussionStatus.NONE }?.let {
                    it.name == entry.value.status
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return CompositionFilterDbResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = CompositionDbResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MsError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyCommentId = CompositionDbResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MsError(
                    code = "id-empty",
                    group = "validation",
                    field = "commentId",
                    message = "CommentId must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = CompositionDbResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MsError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = CompositionDbResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                MsError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
        val resultErrorCommentNotFound = CompositionDbResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                MsError(
                    code = "not-found",
                    field = "commentId",
                    message = "Not Found"
                )
            )
        )

        private fun MsCompositionId.takeIfNotNone(): String? = takeIf {
            it != MsCompositionId.NONE
        }?.asString()

        private fun MsCompositionLock.takeIfNotNone(): String? = takeIf {
            it != MsCompositionLock.NONE
        }?.asString()

        private fun MsCommentId.takeIfNotNone(): String? = takeIf {
            it != MsCommentId.NONE
        }?.asString()
    }
}