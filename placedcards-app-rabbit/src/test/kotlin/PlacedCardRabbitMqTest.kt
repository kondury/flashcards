package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.app.rabbit.config.AppSettings
import com.github.kondury.flashcards.placedcards.app.rabbit.config.ConnectionConfig
import com.github.kondury.flashcards.placedcards.app.rabbit.config.ProcessorConfig
import com.github.kondury.flashcards.placedcards.app.rabbit.config.configure
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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {}

internal class PlacedCardRabbitMqTest {

    companion object {
        private const val RABBIT_IMAGE = "rabbitmq:3.12.6"

        private const val USER_NAME = "test"
        private const val USER_PASSWORD = "test"

        private const val KEY_IN = "in-v1"
        private const val KEY_OUT = "out-v1"
        private const val EXCHANGE = "placedcards-exchange-test"
        private const val EXCHANGE_TYPE = "direct"
        private const val QUEUE = "v1-placedcards-test-queue"
        private const val CONSUMER_TAG = "v1-placedcards-consumer"

        private val placedCardStub = PlacedCardStub.get()

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

    @BeforeTest
    fun tearUp(): Unit = with(appSettings) { controller.start() }

    @AfterTest
    fun tearDown(): Unit = with(appSettings) { controller.close() }

    @Test
    fun `rabbitMq create placed card test`() = testCardCommand(
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
                cardId = placedCardStub.cardId.asString()),
        ),
        responseType = PlacedCardCreateResponse::class.java
    ) { response ->
        assertEquals("create-req", response.requestId)
        assertEquals(placedCardStub.toPlacedCardResponseResource(), response.placedCard)
    }

    @Test
    fun `rabbitMq move placed card test`() = testCardCommand(
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
        responseType = PlacedCardMoveResponse::class.java
    ) { response ->
        assertEquals("move-req", response.requestId)
        assertEquals(placedCardStub.toPlacedCardResponseResource(), response.placedCard)
    }

    @Test
    fun `rabbitMq select placed card test`() = testCardCommand(
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
        responseType = PlacedCardSelectResponse::class.java
    ) { response ->
        assertEquals("select-req", response.requestId)
        assertEquals(placedCardStub.toPlacedCardResponseResource(), response.placedCard)
    }

    @Test
    fun `rabbitMq delete placed card test`() = testCardCommand(
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
        responseType = PlacedCardDeleteResponse::class.java
    ) { response ->
        assertEquals("delete-req", response.requestId)
    }

    @Test
    fun `rabbitMq init placed card test`() = testCardCommand(
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
        responseType = PlacedCardInitResponse::class.java
    ) { response ->
        assertEquals("init-req", response.requestId)
    }

    private inline fun <reified T : IRequest, reified U : IResponse> testCardCommand(
        requestObj: T, responseType: Class<U>, doAssert: (U) -> Unit
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
                apiV1Mapper.writeValueAsBytes(requestObj).let {
                    channel.basicPublish(EXCHANGE, keyIn, null, it)
                }

                runBlocking {
                    withTimeoutOrNull(10.seconds) {
                        while (responseJson.isBlank()) {
                            delay(50)
                        }
                    }
                }

                // then
                apiV1Mapper.readValue(responseJson, responseType).run {
                    doAssert(this)
                }
            }
        }
    }
}