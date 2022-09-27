package ru.otus.music.search.biz.workers

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.initStatus(title: String) =
    worker {
        this.title = title
        on { state == MsState.NONE }
        handle { state = MsState.RUNNING }
    }