package ru.otus.music.search.biz

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Prepare objects to save"
    on { state == MsState.RUNNING }
    handle {
        msRepoRead = msValidated.deepCopy()
        msRepoPrepare = msRepoRead
    }
}
