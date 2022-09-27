package ru.otus.music.search.biz.workers

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.helpers.fail
import ru.otus.music.search.common.models.MsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker


fun ICorChainDsl<MsContext>.stubNoCase(title: String) =
    worker {
        this.title = title
        on { state == MsState.RUNNING }
        handle {
            fail(
                MsError(
                    code = "validation",
                    field = "stub",
                    group = "validation",
                    message = "Wrong stub case is requested: ${stubCase.name}"
                )
            )
        }
    }