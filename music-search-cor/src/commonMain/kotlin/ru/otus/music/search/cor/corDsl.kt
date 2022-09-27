package ru.otus.music.search.cor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.otus.music.search.cor.handlers.CorChainDsl
import ru.otus.music.search.cor.handlers.CorWorkerDsl

@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String

    fun on(function: suspend T.() -> Boolean)

    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): ICorExec<T>
}

@CorDslMarker
interface ICorChainDsl<T>: ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

@CorDslMarker
interface ICorWorkerDsl<T> : ICorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}

fun <T> rootChain(function: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> =
    CorChainDsl<T>().apply(function)

fun <T> ICorChainDsl<T>.chain(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

suspend fun <T> executeParallel(context: T, execs: List<ICorExec<T>>): Unit = coroutineScope {
    execs.forEach {
        launch { it.exec(context) }
    }
}

/**
 * Создает цепочку, элементы которой исполняются параллельно. Будьте аккуратны с доступом к контексту -
 * при необходимости используйте синхронизацию
 */
fun <T> ICorChainDsl<T>.parallel(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>(::executeParallel).apply(function))
}

/**
 * Создает рабочего
 */
fun <T> ICorChainDsl<T>.worker(function: ICorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDsl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}