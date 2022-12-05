package ru.otus.music.search.common.repo.test

import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsCompositionLock
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId

abstract class BaseInitCompositions(val op: String) : IInitObjects<MsCompositionDiscussion> {

    open val lockOld: MsCompositionLock = MsCompositionLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MsCompositionLock = MsCompositionLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: MsUserId = MsUserId("$suf-$op-owner-123"),
        compositionId: MsCompositionId = MsCompositionId("$suf-$op-composition-123"),
        commentId: MsCommentId = MsCommentId("$suf-$op-comment-123"),
        commentAuthorId: MsUserId = MsUserId("$suf-$op-author-123"),
        status: MsDiscussionStatus = MsDiscussionStatus.NONE,
        lock: MsCompositionLock = lockOld,
    ) = MsCompositionDiscussion(
        composition = MsComposition(
            id = compositionId,
            owner = ownerId,
        ),
        comment = MsComment(
            id = commentId,
            author = commentAuthorId,
            text = "$suf $op stub comment",
            status = MsCommentStatus.ACCEPTED
        ),
        status = status,
        lock = lock
    )

    fun createInitCompositionDiscussion(
        suf: String,
        ownerId: MsUserId = MsUserId("$suf-$op-owner-123"),
        compositionId: MsCompositionId = MsCompositionId("$suf-$op-composition-123"),
        commentId: MsCommentId = MsCommentId("$suf-$op-comment-123"),
        commentAuthorId: MsUserId = MsUserId("$suf-$op-author-123"),
        status: MsDiscussionStatus = MsDiscussionStatus.NONE,
        lock: MsCompositionLock = lockOld,
    ) = MsCompositionDiscussion(
        composition = MsComposition(
            id = compositionId,
            owner = ownerId,
        ),
        comments = mutableSetOf(
            MsComment(
                id = commentId,
                author = commentAuthorId,
                text = "$suf $op stub comment",
                lock = lock
            )
        ),
        status = status,
        lock = lock
    )
}
