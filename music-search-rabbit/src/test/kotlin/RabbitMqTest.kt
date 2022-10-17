import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.api.v1.models.*
import ru.otus.music.search.common.EMPTY_FILE
import ru.otus.music.search.config.RabbitConfig
import ru.otus.music.search.config.RabbitExchangeConfiguration
import ru.otus.music.search.controller.RabbitController
import ru.otus.music.search.processor.RabbitDirectProcessor
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RabbitMqTest {
    val container by lazy {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser("guest", "guest")
            start()
        }
    }

    val rabbitMqTestPort: Int by lazy {
        container.getMappedPort(5672)
    }
    val config by lazy {
        RabbitConfig(
            port = rabbitMqTestPort
        )
    }
    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = RabbitExchangeConfiguration(
                keyIn = "in-v1",
                keyOut = "out-v1",
                exchange = exchange,
                queue = "v1-queue",
                consumerTag = "test-tag",
                exchangeType = exchangeType
            )
        )
    }

    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    val mapper = ObjectMapper()

    private val boltCreateV1 = with(MsCompositionDiscussionStub.get()) {
        CompositionCreateRequest(
            composition = CompositionCreateObject(
                file = EMPTY_FILE,
                owner = composition.owner.asString()
            ),
            requestType = "create",
            debug = CompositionDebug(
                mode = CompositionRequestDebugMode.STUB,
                stub = CompositionRequestDebugStubs.SUCCESS
            )
        )
    }
    @BeforeTest
    fun tearUp() {
        controller.start()
    }

    @Test
    fun createCompositionDiscussionTest() {
        val keyOut = processor.processorConfig.keyOut
        val keyIn = processor.processorConfig.keyIn

        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(265L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = mapper.readValue(responseJson, CompositionCreateResponse::class.java)
                val expected = MsCompositionDiscussionStub.get()

                assertEquals(expected.composition.owner.asString(), response.compositionInfo?.composition?.owner)
                assertEquals(expected.composition.file, response.compositionInfo?.composition?.file)
            }
        }
    }
    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
    }
}