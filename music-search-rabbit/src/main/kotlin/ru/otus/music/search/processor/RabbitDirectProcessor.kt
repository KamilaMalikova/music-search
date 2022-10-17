package ru.otus.music.search.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.music.search.biz.MsCompositionProcessor
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.config.RabbitConfig
import ru.otus.music.search.config.RabbitExchangeConfiguration
import kotlinx.datetime.Clock
import ru.otus.music.search.api.v1.models.IRequest
import ru.otus.music.search.common.helpers.addError
import ru.otus.music.search.common.helpers.asMsError
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.mappers.v1.fromTransport
import ru.otus.music.search.mappers.v1.toTransport

class RabbitDirectProcessor(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: MsCompositionProcessor = MsCompositionProcessor(),
) : RabbitProcessorBase(config, processorConfig) {
    private val context = MsContext()

    override suspend fun Channel.processMessage(message: Delivery) {
        context.apply {
            timeStart = Clock.System.now()
        }

        jacksonMapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this).also {
                    println("TYPE: ${this::class.simpleName}")
                }
        }

        val response = processor.exec(context).run { context.toTransport() }
        jacksonMapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")

            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }

    override fun Channel.onError(e: Throwable) {
        e.printStackTrace()
        context.state = MsState.FAILING
        context.addError(error = arrayOf(e.asMsError()))
        val response = context.toTransport()
        jacksonMapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}