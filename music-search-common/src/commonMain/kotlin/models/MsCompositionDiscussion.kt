package ru.otus.music.search.common.models

data class MsCompositionDiscussion(
    var composition: MsComposition = MsComposition(),
    var owner: MsUserId = MsUserId.NONE,
    var comment: MsComment = MsComment(),
    val comments: MutableSet<MsComment> = mutableSetOf(),
    var status: MsDiscussionStatus = MsDiscussionStatus.OPEN
)
