package ru.otus.music.search.common.repo.inmemory.model

import ru.otus.music.search.common.EMPTY_FILE
import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId
import java.io.File

data class CompositionEntity(
    val id: String? = null,
    val owner: String? = null,
    val filePath: String? = null,
    val status: String? = null,
    val comments: Map<String, CommentEntity> = emptyMap(),
    val lock: String? = null
) {
    constructor(model: MsCompositionDiscussion) : this(
        id = model.composition.id.asString().takeIf { it.isNotBlank() },
        owner = model.composition.owner.asString().takeIf { it.isNotBlank() },
        filePath = model.composition.file.absolutePath.takeIf { it.isNotBlank() },
        status = model.status.takeIf { it != MsDiscussionStatus.NONE }?.name,
        comments = model.comments.associate { it.id.asString() to CommentEntity(it) },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal(): MsCompositionDiscussion = MsCompositionDiscussion(
        composition = MsComposition(
            id = id?.let { MsCompositionId(it) } ?: MsCompositionId.NONE,
            owner = owner?.let { MsUserId(it) } ?: MsUserId.NONE,
            file = filePath?.let { File(it) } ?: EMPTY_FILE
        ),
        comments = this.comments.values.map { it.toInternal() }.toMutableSet(),
        status = status?.let { MsDiscussionStatus.valueOf(it) } ?: MsDiscussionStatus.NONE,
        lock = lock?.let { MsCompositionLock(it) } ?: MsCompositionLock.NONE
    )
}

data class CommentEntity(
    val id: String? = null,
    val author: String? = null,
    val text: String? = null,
    val status: String? = null,
) {
    constructor(model: MsComment): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        author = model.author.asString().takeIf { it.isNotBlank() },
        text = model.text.takeIf { it.isNotBlank() },
        status = model.status.name
    )

    fun toInternal(): MsComment = MsComment(
        id = id?.let { MsCommentId(it) } ?: MsCommentId.NONE,
        author = author?.let { MsUserId(it) } ?: MsUserId.NONE,
        text = text ?: "",
        status = status?.let { MsCommentStatus.valueOf(it) } ?: MsCommentStatus.NONE
    )
}