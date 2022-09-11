package ru.otus.music.search

import ru.otus.music.search.common.models.MsComment
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId

object MsCompositionStubBolts {
    val DISCUSSION_BOLT1 = MsCompositionDiscussion(
        composition = MsComposition(
            id = MsCompositionId("1234"),
            owner = MsUserId("567")
        ),
        comment= MsComment(
            id = MsCommentId("987"),
            author = MsUserId("963"),
            text = "Rise of The King",
            status = MsCommentStatus.NONE
        ),
        comments = comments,
        status = MsDiscussionStatus.OPEN
    )

    private val comments: MutableSet<MsComment>
        get() = mutableSetOf(
            MsComment(
                id = MsCommentId("12"),
                author = MsUserId("963"),
                text = "Rise of The Queen",
                status = MsCommentStatus.NONE
            ),
            MsComment(
                id = MsCommentId("13"),
                author = MsUserId("963"),
                text = "Rise of The Vicing",
                status = MsCommentStatus.NONE
            )
        )
}