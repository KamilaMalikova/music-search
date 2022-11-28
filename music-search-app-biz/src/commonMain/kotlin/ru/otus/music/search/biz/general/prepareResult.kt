package ru.otus.music.search.biz.general

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Prepare response"
    on { workMode != MsWorkMode.STUB }
    handle {
        compositionResponse = msRepoDone
        compositionsResponse = msRepoDones
        state = when (val st = state) {
            MsState.RUNNING -> MsState.FINISHING
            else -> st
        }
    }
}