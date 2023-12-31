package com.github.kondury.flashcards.cards.app

import com.github.kondury.flashcards.cards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.app.auth.addAuth
import com.github.kondury.flashcards.cards.app.common.AuthConfig
import com.github.kondury.flashcards.cards.app.helpers.testConfig
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.repository.CardRepository
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


interface V1CardApiContract {

    companion object {
        private const val REQUEST_ID = "12345"
        private const val FRONT_TEXT = "Front text"
        private const val BACK_TEXT = "Back text"
        private const val OLD_UUID = "10000000-0000-0000-0000-000000000001"

        private const val NEW_UUID = "10000000-0000-0000-0000-000000000002"

        private val stub = Card(
            id = CardId(OLD_UUID),
            front = FRONT_TEXT,
            back = BACK_TEXT,
            lock = FcCardLock(OLD_UUID)
        )
    }

    val uuid: String
        get() = NEW_UUID

    val cardStub: Card
        get() = stub

    val initObjects: List<Card>
        get() = listOf(stub)

    fun getRepository(test: String): CardRepository
    val assertSpecificOn: Boolean
    val debugResource: DebugResource

    @Test
    fun create() = testCardCommand<CardCreateRequest, CardCreateResponse>(
        repository = getRepository("create"),
        url = "/v1/card/create",
        requestObj = CardCreateRequest(
            requestId = REQUEST_ID,
            card = CardCreateResource(
                front = FRONT_TEXT,
                back = BACK_TEXT,
            ),
            debug = debugResource
        )
    ) { responseObj ->
        val card = responseObj.card ?: fail("Response attribute card is not expected to be null")
        with(card) {
            assertEquals(NEW_UUID, id)
            assertEquals(FRONT_TEXT, front)
            assertEquals(BACK_TEXT, back)
        }
    }

    @Test
    fun read() = testCardCommand<CardReadRequest, CardReadResponse>(
        repository = getRepository("read"),
        url = "/v1/card/read",
        requestObj = CardReadRequest(
            requestId = REQUEST_ID,
            card = CardReadResource(OLD_UUID),
            debug = debugResource
        )
    ) { responseObj ->
        val card = responseObj.card ?: fail("Response attribute card is not expected to be null")
        with(card) {
            assertEquals(OLD_UUID, id)
        }
    }

    @Test
    fun delete() = testCardCommand<CardDeleteRequest, CardDeleteResponse>(
        repository = getRepository("delete"),
        url = "/v1/card/delete",
        requestObj = CardDeleteRequest(
            requestId = REQUEST_ID,
            card = CardDeleteResource(
                id = OLD_UUID,
                lock = stub.lock.asString(),
            ),
            debug = debugResource
        )
    ) {}

    private inline fun <reified T : IRequest, reified U : IResponse> testCardCommand(
        repository: CardRepository, url: String, requestObj: T, crossinline doAssert: (U) -> Unit = {}
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

    private suspend inline fun <reified T> HttpClient.postWithBody(url: String, body: T): HttpResponse = post(url) {
        contentType(ContentType.Application.Json)
        addAuth(config = AuthConfig.TEST)
        setBody(body)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                setConfig(apiV1Mapper.serializationConfig)
                setConfig(apiV1Mapper.deserializationConfig)
//                enable(SerializationFeature.INDENT_OUTPUT)
//                writerWithDefaultPrettyPrinter()
            }
        }
    }
}