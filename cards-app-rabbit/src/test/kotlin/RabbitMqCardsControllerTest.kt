package com.github.kondury.flashcards.cards.app.rabbit

import com.github.kondury.flashcards.cards.api.v1.apiV1RequestSerialize
import com.github.kondury.flashcards.cards.api.v1.apiV1ResponseDeserialize
import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.app.rabbit.config.AppSettings
import com.github.kondury.flashcards.cards.app.rabbit.config.ConnectionConfig
import com.github.kondury.flashcards.cards.app.rabbit.config.ProcessorConfig
import com.github.kondury.flashcards.cards.app.rabbit.config.configure
import com.github.kondury.flashcards.cards.stubs.CardStub
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.RabbitMQContainer
import kotlin.reflect.KClass
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {}

internal class RabbitMqCardsControllerTest {

    companion object {
        private const val RABBIT_IMAGE = "rabbitmq:3.12.6"

        private const val USER_NAME = "test"
        private const val USER_PASSWORD = "test"

        private const val KEY_IN = "in-v1"
        private const val KEY_OUT = "out-v1"
        private const val EXCHANGE = "cards-exchange-test"
        private const val EXCHANGE_TYPE = "direct"
        private const val QUEUE = "v1-cards-test-queue"
        private const val CONSUMER_TAG = "v1-cards-consumer"

        private val cardStub = CardStub.get()

        private val container = RabbitMQContainer(RABBIT_IMAGE).withExposedPorts(5672, 15672)

        @BeforeAll
        @JvmStatic
        fun beforeAll(): Unit = with(container) {
            start()
            execInContainer("rabbitmqctl", "add_user", USER_NAME, USER_PASSWORD)
            execInContainer("rabbitmqctl", "set_permissions", "-p", "/", USER_NAME, ".*", ".*", ".*")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            container.stop()
        }

        private val appSettings by lazy {
            AppSettings(
                connectionConfig = ConnectionConfig(
                    host = "localhost",
                    port = container.getMappedPort(5672),
                    user = USER_NAME,
                    password = USER_PASSWORD
                ),
                v1ProcessorConfig = ProcessorConfig(
                    keyIn = KEY_IN,
                    keyOut = KEY_OUT,
                    exchange = EXCHANGE,
                    queue = QUEUE,
                    consumerTag = CONSUMER_TAG,
                    exchangeType = EXCHANGE_TYPE,
                ),
            )
        }
    }

    @BeforeTest
    fun tearUp(): Unit = with(appSettings) { controller.start() }

    @AfterTest
    fun tearDown(): Unit = with(appSettings) { controller.stop() }

    @Test
    fun `rabbitMq create card test`() = testCardCommand(
        requestObj = CardCreateRequest(
            requestType = "createCard",
            requestId = "create-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            card = CardCreateResource(
                front = cardStub.front,
                back = cardStub.back,
            ),
        ),
    ) { response: CardCreateResponse ->
        assertEquals("create-req", response.requestId)
        assertEquals(cardStub.front, response.card?.front)
        assertEquals(cardStub.back, response.card?.back)
    }

    @Test
    fun `rabbitMq delete card test`() = testCardCommand(
        requestObj = CardDeleteRequest(
            requestType = "deleteCard",
            requestId = "delete-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            card = CardDeleteResource(
                id = cardStub.id.asString()
            ),
        ),
    ) { response: CardDeleteResponse ->
        assertEquals("delete-req", response.requestId)
    }

    private fun <T : IRequest, U : IResponse> testCardCommand(
        requestObj: T, doAssert: (U) -> Unit
    ) {
        val (keyOut, keyIn) = with(appSettings.v1RabbitProcessor.processorConfig) { Pair(keyOut, keyIn) }
        ConnectionFactory().configure(appSettings.connectionConfig).newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE)

                val queueOut = channel.queueDeclare().queue

                channel.queueBind(queueOut, EXCHANGE, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    logger.info { " [x] Received by $consumerTag: '$responseJson'" }
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback {})

                // when
                logger.info { "Publishing $requestObj" }
                apiV1RequestSerialize(requestObj).let {
                    channel.basicPublish(EXCHANGE, keyIn, null, it.toByteArray())
                }

                runBlocking {
                    withTimeoutOrNull(10.seconds) {
                        while (responseJson.isBlank()) {
                            delay(50)
                        }
                    }
                }

                // then
                apiV1ResponseDeserialize<U>(responseJson).run {
                    doAssert(this)
                }
            }
        }
    }
}