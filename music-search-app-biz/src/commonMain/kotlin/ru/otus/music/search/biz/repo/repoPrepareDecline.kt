package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommentStatus
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoPrepareDecline(title: String) = worker {
    this.title = title
    description = "Preparing data for saving into DB: updating comment"
    on { state == MsState.RUNNING }
    handle {
        msRepoPrepare = msRepoRead.deepCopy().apply {
            comment = msValidated.comment.copy(status = MsCommentStatus.DECLINED)
            lock = msValidated.lock
        }
    }
}