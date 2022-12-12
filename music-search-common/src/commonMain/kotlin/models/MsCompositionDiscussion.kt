package ru.otus.music.search.common.models

import ru.otus.music.search.common.permissions.MsPermissionClient
import ru.otus.music.search.common.permissions.MsPrincipalRelations

data class MsCompositionDiscussion(
    var composition: MsComposition = MsComposition(),
    var comment: MsComment = MsComment(),
    var comments: MutableSet<MsComment> = mutableSetOf(),
    var status: MsDiscussionStatus = MsDiscussionStatus.OPEN,
    var lock: MsCompositionLock = MsCompositionLock.NONE,
    var principalRelations: Set<MsPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<MsPermissionClient> = mutableSetOf(),
) {
    fun deepCopy() = copy(
        comments = comments.toMutableSet()
    )
}
