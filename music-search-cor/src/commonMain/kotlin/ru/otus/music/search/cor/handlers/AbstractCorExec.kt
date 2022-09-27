package ru.otus.music.search.cor.handlers

import ru.otus.music.search.cor.ICorExec
import ru.otus.music.search.cor.ICorExecDsl

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String,
    val blockOn: suspend T.() -> Boolean,
    val blockExcept: suspend T.(ex: Throwable) -> Unit
): ICorExec<T> {
    suspend fun runBlockHandle(context: T, blockHandle: suspend () -> Unit) {
        if (context.blockOn()) {
            try {
                blockHandle()
            } catch (ex: Throwable) {
                context.blockExcept(ex)
            }
        }
    }
}

abstract class CorExecDsl<T> : ICorExecDsl<T> {
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    override var title: String = ""
    override var description: String = ""

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(e: Throwable) -> Unit) {
        blockExcept = function
    }
}