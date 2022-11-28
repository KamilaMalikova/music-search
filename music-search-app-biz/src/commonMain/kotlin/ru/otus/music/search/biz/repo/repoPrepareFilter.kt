package ru.otus.music.search.biz.repo

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoPrepareFilter(title: String) = worker {
    this.title = title
    description = "Preparing search request"
    on { state == MsState.RUNNING }
    handle {
        msRepoPrepare = msRepoRead.deepCopy()
        msRepoDone = msRepoRead.deepCopy()
    }
}