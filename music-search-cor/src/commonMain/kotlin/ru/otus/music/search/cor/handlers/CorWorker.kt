package ru.otus.music.search.cor.handlers

import ru.otus.music.search.cor.CorDslMarker
import ru.otus.music.search.cor.ICorExec
import ru.otus.music.search.cor.ICorWorkerDsl

class CorWorker<T>(
    title: String = "",
    description: String = "",
    private val blockHandle: suspend T.() -> Unit,
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(ex: Throwable) -> Unit = { e -> throw e }
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun exec(context: T) =
        runBlockHandle(context) {
            context.blockHandle()
        }
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )
}
