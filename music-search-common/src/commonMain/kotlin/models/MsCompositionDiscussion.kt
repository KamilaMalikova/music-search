package ru.otus.music.search.common.models

data class MsCompositionDiscussion(
    var composition: MsComposition = MsComposition(),
    var comment: MsComment = MsComment(),
    var comments: MutableSet<MsComment> = mutableSetOf(),
    var status: MsDiscussionStatus = MsDiscussionStatus.OPEN,
    var lock: MsCompositionLock = MsCompositionLock.NONE
) {
    fun deepCopy() = copy(
        comments = comments.toMutableSet()
    )
}
