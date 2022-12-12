package ru.otus.music.search.common.models

data class MsFilter(
    var ownerId: MsUserId = MsUserId.NONE,
    var discussionStatus: MsDiscussionStatus = MsDiscussionStatus.NONE,
    var searchPermissions: MutableSet<MsSearchPermissions> = mutableSetOf(),
)