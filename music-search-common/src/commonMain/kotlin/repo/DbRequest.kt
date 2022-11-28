package ru.otus.music.search.common.repo

import ru.otus.music.search.common.models.*

data class CompositionDiscussionDbRequest(
    val discussion: MsCompositionDiscussion
)

data class CompositionIdDbRequest(
    val id: MsCompositionId,
    val lock: MsCompositionLock = MsCompositionLock.NONE
)

data class CommentIdDbRequest(
    val compositionId: MsCompositionId,
    val commentId: MsCommentId,
    val lock: MsCompositionLock = MsCompositionLock.NONE
)

data class CommentDbRequest(
    val compositionId: MsCompositionId,
    val comment: MsComment,
    val lock: MsCompositionLock = MsCompositionLock.NONE
)

data class CommentUpdateDbRequest(
    val compositionId: MsCompositionId,
    val comment: MsComment,
    val lock: MsCompositionLock = MsCompositionLock.NONE
)

data class CompositionFilterRequest(
    val ownerId: MsUserId = MsUserId.NONE,
    val status: MsDiscussionStatus = MsDiscussionStatus.NONE
)