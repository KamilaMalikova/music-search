package ru.otus.music.search.biz.groups

import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.chain

fun ICorChainDsl<MsContext>.operation(
    title: String,
    command: MsCommand,
    block: ICorChainDsl<MsContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MsState.RUNNING }
}