package ru.otus.music.search.biz.groups

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsWorkMode
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.chain

fun ICorChainDsl<MsContext>.stub(
    title: String,
    block: ICorChainDsl<MsContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.workMode == MsWorkMode.STUB && state == MsState.RUNNING }
}