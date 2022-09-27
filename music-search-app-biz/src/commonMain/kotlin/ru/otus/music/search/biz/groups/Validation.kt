package ru.otus.music.search.biz.groups

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.chain

fun ICorChainDsl<MsContext>.validation(
    block: ICorChainDsl<MsContext>.() -> Unit
) = chain {
    block()
    this.title = "Validation"
    on { state == MsState.RUNNING }
}