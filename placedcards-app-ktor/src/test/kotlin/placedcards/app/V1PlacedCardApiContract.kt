package com.github.kondury.flashcards.placedcards.app

import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.app.auth.addAuth
import com.github.kondury.flashcards.placedcards.app.helpers.authConfig
import com.github.kondury.flashcards.placedcards.app.helpers.testConfig
import com.github.kondury.flashcards.placedcards.common.models.FcBox
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull


interface V1PlacedCardApiContract {

    companion object {
        private const val REQUEST_ID = "12345"
        private const val INIT_UUID = "10000000-0000-0000-0000-000000000001"
        private const val NEW_UUID = "10000000-0000-0000-0000-000000000002"

        private val initTransportBox = Box.NEW
        private val initInternalBox = FcBox.NEW

        private val stub = PlacedCardStub.getWith(
            id = PlacedCardId(INIT_UUID),
            lock = FcPlacedCardLock(INIT_UUID),
            box = initInternalBox
        )
    }

    val uuid: String
        get() = NEW_UUID

    val placedCardStub: PlacedCard
        get() = stub

    val initObjects: List<PlacedCard>
        get() = listOf(stub)

    fun getRepository(test: String): PlacedCardRepository
    val assertSpecificOn: Boolean
    val debugResource: DebugResource

    @Test
    fun create() = testPlacedCardCommand<PlacedCardCreateRequest, PlacedCardCreateResponse>(
        repository = getRepository("create"),
        url = "/v1/placed-card/create",
        requestObj = PlacedCardCreateRequest(
            requestId = REQUEST_ID,
            placedCard = PlacedCardCreateResource(
                box = Box.NEW,
                ownerId = "create-user-id",
                cardId = "new-card-id",
            ),
            debug = debugResource
        )
    ) { responseObj ->
        val placedCard = responseObj.placedCard ?: fail("Response attribute card is not expected to be null")
        with(placedCard) {
            assertEquals(NEW_UUID, id)
            assertEquals(NEW_UUID, lock)
            assertEquals("create-user-id", ownerId)
            assertEquals(Box.NEW, box)
            assertEquals("new-card-id", cardId)
            assertNotNull(createdAt)
            assertNotNull(updatedAt)
            assertEquals(createdAt, updatedAt)
        }
    }

    @Test
    fun delete() = testPlacedCardCommand<PlacedCardDeleteRequest, PlacedCardDeleteResponse>(
        repository = getRepository("delete"),
        url = "/v1/placed-card/delete",
        requestObj = PlacedCardDeleteRequest(
            requestId = REQUEST_ID,
            placedCard = PlacedCardDeleteResource(
                id = stub.id.asString(),
                lock = stub.lock.asString(),
            ),
            debug = debugResource
        )
    ) {}

    @Test
    fun move() = testPlacedCardCommand<PlacedCardMoveRequest, PlacedCardMoveResponse>(
        repository = getRepository("move"),
        url = "/v1/placed-card/move",
        requestObj = PlacedCardMoveRequest(
            requestId = REQUEST_ID,
            move = PlacedCardMoveResource(
                id = stub.id.asString(),
                lock = stub.lock.asString(),
                box = Box.REPEAT,
            ),
            debug = debugResource
        )
    ) { responseObj ->
        val placedCard = responseObj.placedCard ?: fail("Response attribute card is not expected to be null")
        with(placedCard) {
            assertEquals(stub.id.asString(), id)
            assertEquals(stub.createdAt.toString(), createdAt)
            assertNotEquals(stub.updatedAt.toString(), updatedAt)

            require(stub.box != FcBox.REPEAT) {
                "The box assertion only makes sense when the init box is not equal to the final box"
            }
            assertEquals(Box.REPEAT, box)
            require(stub.lock.asString() != NEW_UUID) {
                "The lock assertion only makes sense when the init lock is not equal to the final lock"
            }
            assertEquals(NEW_UUID, lock)
        }
    }

    @Test
    fun select() = testPlacedCardCommand<PlacedCardSelectRequest, PlacedCardSelectResponse>(
        repository = getRepository("select"),
        url = "/v1/placed-card/select",
        requestObj = PlacedCardSelectRequest(
            requestId = REQUEST_ID,
            select = PlacedCardSelectResource(
                ownerId = stub.ownerId.asString(),
                box = initTransportBox,
                searchStrategy = SearchStrategy.EARLIEST_CREATED
            ),
            debug = debugResource
        )
    ) { responseObj ->
        val placedCard = responseObj.placedCard ?: fail("Response attribute card is not expected to be null")
        with(placedCard) {
            assertEquals(INIT_UUID, id)
            assertEquals(INIT_UUID, lock)
            assertEquals(stub.ownerId.asString(), ownerId)
            assertEquals(initTransportBox, box)
            assertEquals(stub.cardId.asString(), cardId)
            assertEquals(stub.createdAt.toString(), createdAt)
            assertEquals(stub.updatedAt.toString(), updatedAt)
        }
    }

    private inline fun <reified T : IRequest, reified U : IResponse> testPlacedCardCommand(
        repository: PlacedCardRepository,
        url: String,
        requestObj: T,
        crossinline doAssert: (U) -> Unit = {}
    ) = testApplication {
        application {
            moduleJvm(testConfig(repository))
        }
        val response = myClient().postWithBody(url, requestObj)
        val responseObj = response.body<U>()
        val status = response.status
        assertEquals(HttpStatusCode.OK, status, "Http code expected: ${HttpStatusCode.OK}, actual: $status")
        val requestId = responseObj.requestId
        assertEquals(REQUEST_ID, requestId, "Request id expected: '$REQUEST_ID', actual: $requestId")
        if (assertSpecificOn) {
            doAssert(responseObj)
        }
    }

    private suspend inline fun <reified T> HttpClient.postWithBody(url: String, body: T): HttpResponse =
        post(url) {
            contentType(ContentType.Application.Json)
            addAuth(config = authConfig)
            setBody(body)
        }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                setConfig(apiV1Mapper.serializationConfig)
                setConfig(apiV1Mapper.deserializationConfig)
            }
        }
    }
}