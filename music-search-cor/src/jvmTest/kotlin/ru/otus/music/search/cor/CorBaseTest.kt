package ru.otus.music.search.cor

import kotlinx.coroutines.runBlocking
import ru.otus.music.search.cor.handlers.CorChain
import ru.otus.music.search.cor.handlers.CorWorker
import ru.otus.music.search.cor.handlers.executeSequential
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorBaseTest {
    @Test
    fun `test should execute handle`() = runBlocking {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1" }
        )

        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("w1", ctx.history)
    }

    @Test
    fun `test worker should not execute handle`() = runBlocking {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1" },
            blockOn = { status == CorStatus.ERROR }
        )

        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("", ctx.history)
    }

    @Test
    fun `test worker should handle exception`() = runBlocking {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { throw RuntimeException("some error") },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("some error", ctx.history)
    }

    @Test
    fun `chain should execute workers`() = runBlocking {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == CorStatus.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain<TestContext>(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
            handler = ::executeSequential
        )
        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("w1; w2; ", ctx.history)
    }

    private fun execute(dsl: ICorExecDsl<TestContext>): TestContext = runBlocking {
        val ctx = TestContext()
        dsl.build().exec(ctx)
        ctx
    }

    @Test
    fun `handle should execute`() {
        assertEquals("w1; ", execute(rootChain {
            worker {
                handle { history += "w1; " }
            }
        }).history)
    }

    @Test
    fun `on should check condition`() {
        assertEquals("w2; w3; ", execute(rootChain {
            worker {
                on { status == CorStatus.ERROR }
                handle { history += "w1; " }
            }
            worker {
                on { status == CorStatus.NONE }
                handle {
                    history += "w2; "
                    status = CorStatus.FAILING
                }
            }
            worker {
                on { status == CorStatus.FAILING }
                handle { history += "w3; " }
            }
        }).history)
    }

    @Test
    fun `except should execute when exception`() {
        assertEquals("some error", execute(rootChain {
            worker {
                handle { throw RuntimeException("some error") }
                except { history += it.message }
            }
        }).history)
    }

    @Test
    fun `should throw when exception and no except`() {
        assertFails {
            execute(rootChain {
                worker("throw") { throw RuntimeException("some error") }
            })
        }
    }

    @Test
    fun `complex chain example`() = runBlocking {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == CorStatus.NONE }
                handle { status = CorStatus.RUNNING }
                except { status = CorStatus.ERROR }
            }

            chain {
                on { status == CorStatus.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some += 4
                }
            }

            parallel {
                on {
                    some < 15
                }

                worker(title = "Increment some") {
                    some++
                }
            }

            printResult()

        }.build()

        val ctx = TestContext()
        chain.exec(ctx)
        println("Complete: $ctx")
    }
}

private fun ICorChainDsl<TestContext>.printResult() = worker(title = "Print example") {
    println("some = $some")
}

data class TestContext(
    var status: CorStatus = CorStatus.NONE,
    var some: Int = 0,
    var history: String = ""
)

enum class CorStatus {
    NONE,
    RUNNING,
    FAILING,
    DONE,
    ERROR
}