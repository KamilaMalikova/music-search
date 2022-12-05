package ru.otus.music.search.repo.cassandra.model

import ru.otus.music.search.common.models.MsDiscussionStatus

enum class DiscussionStatus {
    OPEN,
    CLOSED
}

fun DiscussionStatus?.fromTransport() = when(this) {
    null -> MsDiscussionStatus.NONE
    DiscussionStatus.OPEN -> MsDiscussionStatus.OPEN
    DiscussionStatus.CLOSED -> MsDiscussionStatus.CLOSED
}

fun MsDiscussionStatus.toTransport() = when(this) {
    MsDiscussionStatus.NONE -> null
    MsDiscussionStatus.OPEN -> DiscussionStatus.OPEN
    MsDiscussionStatus.CLOSED -> DiscussionStatus.CLOSED
}