package ru.otus.music.search.repo.cassandra.model

import ru.otus.music.search.common.models.MsCommentStatus

enum class CommentStatus {
    ACCEPTED,
    DECLINED
}

fun CommentStatus?.fromTransport() = when(this) {
    null -> MsCommentStatus.NONE
    CommentStatus.ACCEPTED -> MsCommentStatus.ACCEPTED
    CommentStatus.DECLINED -> MsCommentStatus.DECLINED
}

fun MsCommentStatus.toTransport() = when(this) {
    MsCommentStatus.NONE -> null
    MsCommentStatus.ACCEPTED -> CommentStatus.ACCEPTED
    MsCommentStatus.DECLINED -> CommentStatus.DECLINED
}