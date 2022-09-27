package ru.otus.music.search.cor.handlers

import ru.otus.music.search.cor.CorDslMarker
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.ICorExec
import ru.otus.music.search.cor.ICorExecDsl

class CorChain<T>(
    title: String = "",
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(ex: Throwable) -> Unit = { e -> throw e },
    private val execs: List<ICorExec<T>>,
    private val handler: suspend (T, List<ICorExec<T>>) -> Unit,
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun exec(context: T) =
        runBlockHandle(context) { handler(context, execs) }
}

suspend fun <T> executeSequential(context: T, execs: List<ICorExec<T>>, ) {
    execs.forEach { it.exec(context) }
}

@CorDslMarker
class CorChainDsl<T>(
    private val handler: suspend (T, List<ICorExec<T>>) -> Unit = ::executeSequential,
) : CorExecDsl<T>(), ICorChainDsl<T> {
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf()
    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        handler = handler,
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}
