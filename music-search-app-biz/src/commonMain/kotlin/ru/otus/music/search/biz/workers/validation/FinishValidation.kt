package ru.otus.music.search.biz.workers.validation

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.finishValidation(title: String) =
    worker {
        this.title = title
        on { state == MsState.RUNNING }
        handle {
            msValidated = msValidating
        }
    }

fun ICorChainDsl<MsContext>.finishFilterValidation(title: String) =
    worker {
        this.title = title
        on { state == MsState.RUNNING }
        handle {
            filterValidated = filterValidating
        }
    }