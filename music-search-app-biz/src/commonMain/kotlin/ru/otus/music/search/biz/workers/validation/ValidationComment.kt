package ru.otus.music.search.biz.workers.validation

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.fail
import ru.otus.music.search.common.models.MsCommentId
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.validateAuthorIdNotEmpty(title: String) =
    worker {
        this.title = title
        on { msValidating.comment.author == MsUserId.NONE }
        handle {
            fail(
                type = "empty",
                field = "comment.author"
            )
        }
    }

fun ICorChainDsl<MsContext>.validateCommentNotEmpty(title: String) =
    worker {
        this.title = title
        on { msValidating.comment.text.isEmpty() }
        handle {
            fail(
                type = "empty",
                field = "comment.text"
            )
        }
    }

fun ICorChainDsl<MsContext>.validateCommentHasContent(title: String) =
    worker {
        this.title = title
        val regExp = Regex("\\p{L}")
        on { msValidating.comment.text.isNotEmpty() && !msValidating.comment.text.contains(regExp) }
        handle {
            fail(
                type = "no-content",
                field = "comment.text"
            )
        }
    }

fun ICorChainDsl<MsContext>.validateCommentIdNotEmpty(title: String) =
    worker {
        this.title = title
        on { msValidating.comment.id == MsCommentId.NONE }
        handle {
            fail(
                type = "empty",
                field = "comment.id"
            )
        }
    }