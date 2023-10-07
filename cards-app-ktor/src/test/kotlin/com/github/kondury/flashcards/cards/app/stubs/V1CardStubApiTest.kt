package com.github.kondury.flashcards.cards.app.stubs

import com.github.kondury.flashcards.cards.api.v1.apiV1Mapper
import com.github.kondury.flashcards.cards.api.v1.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class V1CardStubApiTest {

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
    fun `test createCard command`() = testCardCommand<CardCreateRequest, CardCreateResponse>(
        "/v1/card/create",
        CardCreateRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            card = CardCreateResource(front = "Front text", back = "Back text"),
        )
    )

    @Test
    fun `test readCard command`() = testCardCommand<CardReadRequest, CardReadResponse>(
        "/v1/card/read",
        CardReadRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            card = CardReadResource(id = "CardId"),
        )
    )

    @Test
    fun `test deleteCard command`() = testCardCommand<CardDeleteRequest, CardDeleteResponse>(
        "/v1/card/delete",
        CardDeleteRequest(
            requestId = REQUEST_ID,
            debug = debugResource,
            card = CardDeleteResource(id = "CardId"),
        )
    )

    private inline fun <reified T : IRequest, reified U : IResponse> testCardCommand(
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
