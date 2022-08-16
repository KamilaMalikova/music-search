package ru.otus.music.search.common.models

data class MsFilter(
    var creatorId: MsUserId = MsUserId.NONE,
    var discussionStatus: MsDiscussionStatus = MsDiscussionStatus.NONE
)