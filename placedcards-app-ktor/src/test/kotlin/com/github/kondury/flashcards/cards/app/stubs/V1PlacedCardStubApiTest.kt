package com.github.kondury.flashcards.placedcards.app.stubs

import com.github.kondury.flashcards.placedcards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.placedcards.api.v1.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.*
import org.junit.Test

class V1PlacedCardStubApiTest {

    companion object {
        const val REQUEST_ID = "12345"
        private val debugResource = DebugResource(
            mode = RunMode.STUB,
            stub = DebugStub.SUCCESS,
        )

        private fun assertCommon(status: HttpStatusCode, requestId: String?) {
            assertEquals(
                HttpStatusCode.OK, status,
                "Http code expected: ${HttpStatusCode.OK}, actual: $status"
            )
            assertEquals(
                REQUEST_ID, requestId,
                "Request id expected: '$REQUEST_ID', actual: $requestId"
            )
        }
    }

    @Test
    fun `test createPlacedCard command`() = testPlacedCardCommand<PlacedCardCreateRequest, PlacedCardCreateResponse>(
        "/v1/placed-card/create",
        PlacedCardCreateRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            placedCard = PlacedCardCreateResource(box = Box.NEW, ownerId = "OwnerId", cardId = "CardId"),
        )
    )

    @Test
    fun `test movePlacedCard command`() = testPlacedCardCommand<PlacedCardMoveRequest, PlacedCardMoveResponse>(
        "/v1/placed-card/move",
        PlacedCardMoveRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            move = PlacedCardMoveResource(id = "PlacedCardId", box = Box.REPEAT),
        )
    )

    @Test
    fun `test deletePlacedCard command`() = testPlacedCardCommand<PlacedCardDeleteRequest, PlacedCardDeleteResponse>(
        "/v1/placed-card/delete",
        PlacedCardDeleteRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            placedCard = PlacedCardDeleteResource(id = "PlacedCardId"),
        )
    )

    @Test
    fun `test selectPlacedCard command`() = testPlacedCardCommand<PlacedCardSelectRequest, PlacedCardSelectResponse>(
        "/v1/placed-card/select",
        PlacedCardSelectRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            select = PlacedCardSelectResource(
                ownerId = "OwnerId",
                box = Box.NEW,
                searchStrategy = SearchStrategy.EARLIEST_CREATED,
            ),
        )
    )

    @Test
    fun `test initPlacedCard command`() = testPlacedCardCommand<PlacedCardInitRequest, PlacedCardInitResponse>(
        "/v1/placed-card/init",
        PlacedCardInitRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            init = PlacedCardInitResource(ownerId = "OwnerId", box = Box.NEW),
        ),
    )

    private inline fun <reified T : IRequest, reified U : IResponse> testPlacedCardCommand(
        url: String,
        requestObj: T,
    ) = testApplication {
        val response = myClient().postWithBody(url, requestObj)
        val responseObj = response.body<U>()
        assertCommon(response.status, responseObj.requestId)
    }

    private suspend inline fun <reified T> HttpClient.postWithBody(url: String, body: T): HttpResponse =
        post(url) {
            contentType(ContentType.Application.Json)
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
