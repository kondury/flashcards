package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.app.rabbit.ConnectionConfig
import com.github.kondury.flashcards.app.rabbit.ProcessorConfig
import com.github.kondury.flashcards.app.rabbit.configure
import com.github.kondury.flashcards.placedcards.api.v1.apiV1RequestSerialize
import com.github.kondury.flashcards.placedcards.api.v1.apiV1ResponseDeserialize
import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.mappers.v1.toPlacedCardResponseResource
import com.github.kondury.flashcards.placedcards.mappers.v1.toTransportPlacedCard
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {}

internal class RabbitMqPlacedCardControllerTest {

    companion object {
        private const val RABBIT_IMAGE = "rabbitmq:3.12.6"

        private const val USER_NAME = "test"
        private const val USER_PASSWORD = "test"

        private const val KEY_IN = "in-v1"
        private const val KEY_OUT = "out-v1"
        private const val EXCHANGE = "placedcards-exchange-test"
        private const val EXCHANGE_TYPE = "direct"
        private const val QUEUE = "v1-placedcards-test-queue"
        private const val CONSUMER_TAG = "v1-placedcards-test-consumer"

        private val placedCardStub = PlacedCardStub.get()

        private val container = RabbitMQContainer(RABBIT_IMAGE).withExposedPorts(5672, 15672)

        @BeforeAll
        @JvmStatic
        fun beforeAll(): Unit = with(container) {
            start()
            execInContainer("rabbitmqctl", "add_user", USER_NAME, USER_PASSWORD)
            execInContainer("rabbitmqctl", "set_permissions", "-p", "/", USER_NAME, ".*", ".*", ".*")
            with(placedCardsRabbitConfig) {
                controller.start()
            }
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            with(placedCardsRabbitConfig) {
                controller.stop()
            }
            container.stop()
        }

        private val placedCardsRabbitConfig by lazy {
            PlacedCardsRabbitConfig(
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

    @Test
    fun `rabbitMq create placed card test`() = testPlacedCardCommand(
        requestObj = PlacedCardCreateRequest(
            requestType = "createPlacedCard",
            requestId = "create-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            placedCard = PlacedCardCreateResource(
                box = placedCardStub.box.toTransportPlacedCard(),
                ownerId = placedCardStub.ownerId.asString(),
                cardId = placedCardStub.cardId.asString()
            ),
        ),
    ) { response: PlacedCardCreateResponse ->
        assertEquals("create-req", response.requestId)
        assertEquals(placedCardStub.toPlacedCardResponseResource(), response.placedCard)
    }

    @Test
    fun `rabbitMq move placed card test`() = testPlacedCardCommand(
        requestObj = PlacedCardMoveRequest(
            requestType = "movePlacedCard",
            requestId = "move-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            move = PlacedCardMoveResource(
                id = placedCardStub.id.asString(),
                box = placedCardStub.box.toTransportPlacedCard(),
            ),
        ),
    ) { response: PlacedCardMoveResponse ->
        assertEquals("move-req", response.requestId)
        assertEquals(placedCardStub.toPlacedCardResponseResource(), response.placedCard)
    }

    @Test
    fun `rabbitMq select placed card test`() = testPlacedCardCommand(
        requestObj = PlacedCardSelectRequest(
            requestType = "selectPlacedCard",
            requestId = "select-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            select = PlacedCardSelectResource(
                ownerId = placedCardStub.ownerId.asString(),
                box = placedCardStub.box.toTransportPlacedCard(),
                searchStrategy = null
            ),
        ),
    ) { response: PlacedCardSelectResponse ->
        assertEquals("select-req", response.requestId)
        assertEquals(placedCardStub.toPlacedCardResponseResource(), response.placedCard)
    }

    @Test
    fun `rabbitMq delete placed card test`() = testPlacedCardCommand(
        requestObj = PlacedCardDeleteRequest(
            requestType = "deletePlacedCard",
            requestId = "delete-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            placedCard = PlacedCardDeleteResource(
                id = placedCardStub.id.asString()
            ),
        ),
    ) { response: PlacedCardDeleteResponse ->
        assertEquals("delete-req", response.requestId)
    }

    @Test
    fun `rabbitMq init placed card test`() = testPlacedCardCommand(
        requestObj = PlacedCardInitRequest(
            requestType = "initPlacedCard",
            requestId = "init-req",
            debug = DebugResource(
                mode = RunMode.STUB,
                stub = DebugStub.SUCCESS
            ),
            init = PlacedCardInitResource(
                ownerId = placedCardStub.ownerId.asString(),
                box = placedCardStub.box.toTransportPlacedCard(),
            ),
        ),
    ) { response: PlacedCardInitResponse ->
        assertEquals("init-req", response.requestId)
    }

    private inline fun <reified T : IRequest, U : IResponse> testPlacedCardCommand(
        requestObj: T, doAssert: (U) -> Unit
    ) {
        ConnectionFactory().configure(placedCardsRabbitConfig.connectionConfig).newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE)

                val queueOut = channel.queueDeclare().queue

                channel.queueBind(queueOut, EXCHANGE, KEY_OUT)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    logger.info { " [x] Received by $consumerTag: '$responseJson'" }
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback {})

                // when
                logger.info { "Publishing $requestObj" }
                apiV1RequestSerialize(requestObj).let {
                    channel.basicPublish(EXCHANGE, KEY_IN, null, it.toByteArray())
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