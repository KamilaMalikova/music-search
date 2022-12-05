package ru.otus.music.search.common.models

data class MsComment(
    var id: MsCommentId = MsCommentId.NONE,
    var author: MsUserId = MsUserId.NONE,
    var text: String = "",
    var status: MsCommentStatus = MsCommentStatus.NONE,
    var lock: MsCompositionLock = MsCompositionLock.NONE
) {
    companion object{
        val NONE = MsComment()
    }
}
