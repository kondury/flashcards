package com.github.kondury.flashcards.api.v1

import com.github.kondury.flashcards.api.v1.models.*
import com.github.kondury.flashcards.api.v1.testutils.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class CardSerializationTest {

    companion object {

        @JvmStatic
        fun requestSerializationData(): List<Arguments> {
            return listOf(
                Arguments.of("createCard", cardCreateRequest, cardCreateRequestJsonChecks),
                Arguments.of("deleteCard", cardDeleteRequest, cardDeleteRequestJsonChecks),
                Arguments.of("readCard", cardReadRequest, cardReadRequestJsonChecks),
            )
        }

        @JvmStatic
        fun responseSerializationData(): List<Arguments> {
            return listOf(
                Arguments.of("createCard", cardCreateResponse, cardCreateResponseJsonChecks),
                Arguments.of("deleteCard", cardDeleteResponse, cardDeleteResponseJsonChecks),
                Arguments.of("readCard", cardReadResponse, cardReadResponseJsonChecks),
            )
        }


        // card requests data

        private val cardCreateRequest = CardCreateRequest(
            requestId = "mockedRequestId",
            debug = debug,
            card = CardCreateResource(front = "Front text", back = "Back text"),
        )

        private val cardCreateRequestJsonChecks = listOf(
            "\"requestType\":\"createCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"front\":\"Front text\"",
            "\"back\":\"Back text\"",
        )

        private val cardDeleteRequest = CardDeleteRequest(
            requestId = "mockedRequestId",
            debug = debug,
            card = CardDeleteResource(id = "cardId", lock = "LockType"),
        )

        private val cardDeleteRequestJsonChecks = listOf(
            "\"requestType\":\"deleteCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"id\":\"cardId\"",
            "\"lock\":\"LockType\"",
        )

        private val cardReadRequest = CardReadRequest(
            requestId = "mockedRequestId",
            debug = debug,
            card = CardReadResource(id = "cardId"),
        )

        private val cardReadRequestJsonChecks = listOf(
            "\"requestType\":\"readCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"id\":\"cardId\"",
        )

        // card responses data
        private val cardResponseJsonChecks = listOf(
            "\"requestId\":\"mockedRequestId\"",
            "\"id\":\"cardId\"",
            "\"front\":\"Front text\"",
            "\"back\":\"Back text\"",
        )

        private val cardResponseResource = CardResponseResource(
            id = "cardId",
            front = "Front text",
            back = "Back text",
        )

        private val cardCreateResponse = CardCreateResponse(
            requestId = "mockedRequestId",
            errors = listOf(error),
            card = cardResponseResource,
        )

        private val cardCreateResponseJsonChecks =
            listOf("\"responseType\":\"createCard\"") + cardResponseJsonChecks + errorJsonChecks

        private val cardReadResponse = CardReadResponse(
            requestId = "mockedRequestId",
            errors = listOf(error),
            card = cardResponseResource,
        )

        private val cardReadResponseJsonChecks =
            listOf("\"responseType\":\"readCard\"") + cardResponseJsonChecks + errorJsonChecks

        private val cardDeleteResponse = CardDeleteResponse(
            requestId = "mockedRequestId",
            errors = listOf(error),
        )

        private val cardDeleteResponseJsonChecks = listOf(
            "\"responseType\":\"deleteCard\"",
            "\"requestId\":\"mockedRequestId\"",
        )
    }

    @ParameterizedTest
    @MethodSource("requestSerializationData")
    fun `Card requests serialization`(requestType: String, request: IRequest, jsonSubstrings: List<String>) {
        val json = apiV1RequestSerialize(request)
        val jsonAssertions = generateJsonAssertions(jsonSubstrings, json)
        assertJson(requestType, json, jsonAssertions)
    }

    @ParameterizedTest
    @MethodSource("responseSerializationData")
    fun `Card responses serialization`(responseType: String, response: IResponse, jsonSubstrings: List<String>) {
        val json = apiV1ResponseSerialize(response)
        val jsonAssertions = generateJsonAssertions(jsonSubstrings, json)
        assertJson(responseType, json, jsonAssertions)
    }



    @Test
    fun `CardCreateRequest deserialization`() {
        val json = """
            {
                "requestType": "createCard",
                "requestId": "mockedRequestId",
                "debug": {
                    "mode": "test",
                    "stub": "success"
                },
                "card": {
                    "front": "Front text",
                    "back": "Back text"
                }
            }
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as CardCreateRequest
        assertEquals(cardCreateRequest.copy(requestType = "createCard"), result)
    }

    @Test
    fun `CardDeleteRequest deserialization`() {
        val json = """
            {
                "requestType": "deleteCard",
                "requestId": "mockedRequestId",
                "debug": {
                    "mode": "test",
                    "stub": "success"
                },
                "card": {
                    "id": "cardId",
                    "lock": "LockType"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as CardDeleteRequest
        assertEquals(cardDeleteRequest.copy(requestType = "deleteCard"), result)
    }

    @Test
    fun `CardReadRequest deserialization`() {
        val json = """
            {
                "requestType": "readCard",
                "requestId": "mockedRequestId",
                "debug": {
                    "mode": "test",
                    "stub": "success"
                },
                "card": {
                    "id": "cardId"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as CardReadRequest
        assertEquals(cardReadRequest.copy(requestType = "readCard"), result)
    }

    @Test
    fun `CardCreateResponse deserialization`() {
        val json = """
            {
                "responseType":"createCard",
                "requestId":"mockedRequestId",
                "errors": [
                    {
                        "code":"errorCode",
                        "group":"errorGroup",
                        "field":"errorField",
                        "message":"errorMessage"
                    }
                ],
                "card": {
                    "front":"Front text",
                    "back":"Back text",
                    "id":"cardId"
                }
            }
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as CardCreateResponse
        assertEquals(cardCreateResponse.copy(responseType = "createCard"), result)
    }

    @Test
    fun `CardDeleteResponse deserialization`() {
        val json = """
            {
                "responseType":"deleteCard",
                "requestId":"mockedRequestId",
                "errors": [
                    {
                        "code":"errorCode",
                        "group":"errorGroup",
                        "field":"errorField",
                        "message":"errorMessage"
                    }
                ]
            }     
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as CardDeleteResponse
        assertEquals(cardDeleteResponse.copy(responseType = "deleteCard"), result)
    }

    @Test
    fun `CardReadResponse deserialization`() {
        val json = """
            {
                "responseType":"readCard",
                "requestId":"mockedRequestId",
                "errors": [
                    {
                        "code":"errorCode",
                        "group":"errorGroup",
                        "field":"errorField",
                        "message":"errorMessage"
                    }
                ],
                "card": {
                    "front":"Front text",
                    "back":"Back text",
                    "id":"cardId"
                }
            }    
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as CardReadResponse
        assertEquals(cardReadResponse.copy(responseType = "readCard"), result)
    }

}
