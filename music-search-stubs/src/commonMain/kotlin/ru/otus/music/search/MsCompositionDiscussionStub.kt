package ru.otus.music.search

import ru.otus.music.search.MsCompositionStubBolts.DISCUSSION_BOLT1
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionDiscussion
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsDiscussionStatus
import ru.otus.music.search.common.models.MsUserId

object MsCompositionDiscussionStub {
    fun get(): MsCompositionDiscussion = DISCUSSION_BOLT1

    fun prepareSearchList(status: MsDiscussionStatus, owner: MsUserId) = mutableListOf(
        prepareDiscussion(DISCUSSION_BOLT1, MsComposition(MsCompositionId("1"), owner), status),
        prepareDiscussion(DISCUSSION_BOLT1, MsComposition(MsCompositionId("2"), owner), status),
        prepareDiscussion(DISCUSSION_BOLT1, MsComposition(MsCompositionId("3"), owner), status),
        prepareDiscussion(DISCUSSION_BOLT1, MsComposition(MsCompositionId("4"), owner), status),
    )

    private fun prepareDiscussion(
        base: MsCompositionDiscussion,
        composition: MsComposition,
        status: MsDiscussionStatus
    ) = base.copy(
        composition = composition,
        status = status
    )
}