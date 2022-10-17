package ru.otus.music.search.controller

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.otus.music.search.processor.RabbitProcessorBase
import java.util.concurrent.Executors

class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) {
    private val scope = CoroutineScope(
        Executors.newSingleThreadExecutor()
            .asCoroutineDispatcher() + CoroutineName("thread-rabbitmq-controller")
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun start() = scope.launch {
        processors.forEach {
            launch(
                Dispatchers.IO
                    .limitedParallelism(1) + CoroutineName("thread-${it.processorConfig.consumerTag}")
            ) {
                try {
                    it.process()
                } catch (ex: RuntimeException) {
                    ex.printStackTrace()
                }
            }
        }
    }
}