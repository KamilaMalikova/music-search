package ru.otus.music.search

import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.config.RabbitConfig
import ru.otus.music.search.config.RabbitExchangeConfiguration
import ru.otus.music.search.controller.RabbitController
import ru.otus.music.search.processor.RabbitDirectProcessor

fun main() {
    val config = RabbitConfig()
    val adProcessor = MsCompositionProcessor()

    val producerConfig = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange",
        queue = "v1-queue",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = producerConfig,
            processor = adProcessor
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}