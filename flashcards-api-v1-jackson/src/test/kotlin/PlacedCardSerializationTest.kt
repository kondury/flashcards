package com.github.kondury.flashcards.api.v1

import com.github.kondury.flashcards.api.v1.models.*
import com.github.kondury.flashcards.api.v1.testutils.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class PlacedCardSerializationTest {

    companion object {

        @JvmStatic
        fun requestSerializationData(): List<Arguments> = listOf(
            Arguments.of("createPlacedCard", placedCardCreateRequest, placedCardCreateRequestJsonChecks),
            Arguments.of("deletePlacedCard", placedCardDeleteRequest, placedCardDeleteRequestJsonChecks),
            Arguments.of("initPlacedCard", placedCardInitRequest, placedCardInitRequestJsonChecks),
            Arguments.of("movePlacedCard", placedCardMoveRequest, placedCardMoveRequestJsonChecks),
            Arguments.of("selectPlacedCard", placedCardSelectRequest, placedCardSelectRequestJsonChecks),
        )

        @JvmStatic
        fun responseSerializationData(): List<Arguments> = listOf(
            Arguments.of("createPlacedCard", placedCardCreateResponse, placedCardCreateResponseJsonChecks),
            Arguments.of("deletePlacedCard", placedCardDeleteResponse, placedCardDeleteResponseJsonChecks),
            Arguments.of("initPlacedCard", placedCardInitResponse, placedCardInitResponseJsonChecks),
            Arguments.of("movePlacedCard", placedCardMoveResponse, placedCardMoveResponseJsonChecks),
            Arguments.of("selectPlacedCard", placedCardSelectResponse, placedCardSelectResponseJsonChecks),
        )

        // placed card requests data
        private val placedCardCreateRequest = PlacedCardCreateRequest(
            requestId = "mockedRequestId",
            debug = debug,
            placedCard = PlacedCardCreateResource(
                box = Box.NEW,
                ownerId = "OwnerId",
                cardId = "CardId",
            ),
        )
        private val placedCardCreateRequestJsonChecks = listOf(
            "\"requestType\":\"createPlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"box\":\"new\"",
            "\"ownerId\":\"OwnerId\"",
            "\"cardId\":\"CardId\"",
        )
        private val placedCardDeleteRequest = PlacedCardDeleteRequest(
            requestId = "mockedRequestId",
            debug = debug,
            placedCard = PlacedCardDeleteResource(id = "PlacedCardId", lock = "LockType"),
        )
        private val placedCardDeleteRequestJsonChecks = listOf(
            "\"requestType\":\"deletePlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"id\":\"PlacedCardId\"",
            "\"lock\":\"LockType\"",
        )
        private val placedCardInitRequest = PlacedCardInitRequest(
            requestId = "mockedRequestId",
            debug = debug,
            init = PlacedCardInitResource(ownerId = "OwnerId", box = Box.NEW),
        )
        private val placedCardInitRequestJsonChecks = listOf(
            "\"requestType\":\"initPlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"ownerId\":\"OwnerId\"",
            "\"box\":\"new\"",
        )
        private val placedCardMoveRequest = PlacedCardMoveRequest(
            requestId = "mockedRequestId",
            debug = debug,
            move = PlacedCardMoveResource(id = "PlacedCardId", box = Box.REPEAT),
        )
        private val placedCardMoveRequestJsonChecks = listOf(
            "\"requestType\":\"movePlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"id\":\"PlacedCardId\"",
            "\"box\":\"repeat\"",
        )
        private val placedCardSelectRequest = PlacedCardSelectRequest(
            requestId = "mockedRequestId",
            debug = debug,
            select = PlacedCardSelectResource(
                ownerId = "OwnerId",
                box = Box.REPEAT,
                searchStrategy = SearchStrategy.EARLIEST_CREATED
            ),
        )
        private val placedCardSelectRequestJsonChecks = listOf(
            "\"requestType\":\"selectPlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"mode\":\"test\"",
            "\"stub\":\"success\"",
            "\"ownerId\":\"OwnerId\"",
            "\"box\":\"repeat\"",
            "\"searchStrategy\":\"EarliestCreated\"",
        )

        // placed card responses data
        private val placedCardResponseResource = PlacedCardResponseResource(
            id = "PlacedCardId",
            box = Box.NEW,
            ownerId = "OwnerId",
            cardId = "CardId",
            createdOn = "2023-09-25T10:00:00Z",
            updatedOn = "2023-10-25T10:00:00Z",
        )
        private val placedCardResponseJsonChecks = listOf(
            "\"requestId\":\"mockedRequestId\"",
            "\"id\":\"PlacedCardId\"",
            "\"box\":\"new\"",
            "\"ownerId\":\"OwnerId\"",
            "\"createdOn\":\"2023-09-25T10:00:00Z\"",
            "\"updatedOn\":\"2023-10-25T10:00:00Z\"",
        )
        private val placedCardCreateResponse = PlacedCardCreateResponse(
            requestId = "mockedRequestId",
            errors = listOf(error),
            placedCard = placedCardResponseResource,
        )
        private val placedCardCreateResponseJsonChecks =
            listOf("\"responseType\":\"createPlacedCard\"") + placedCardResponseJsonChecks + errorJsonChecks

        private val placedCardDeleteResponse = PlacedCardDeleteResponse(
            requestId = "mockedRequestId",
            result = ResponseResult.ERROR
        )
        private val placedCardDeleteResponseJsonChecks = listOf(
            "\"responseType\":\"deletePlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"result\":\"error\"",
        )
        private val placedCardInitResponse = PlacedCardInitResponse(
            requestId = "mockedRequestId",
            result = ResponseResult.SUCCESS
        )
        private val placedCardInitResponseJsonChecks = listOf(
            "\"responseType\":\"initPlacedCard\"",
            "\"requestId\":\"mockedRequestId\"",
            "\"result\":\"success\""
        )
        private val placedCardMoveResponse = PlacedCardMoveResponse(
            requestId = "mockedRequestId",
            result = ResponseResult.SUCCESS,
            placedCard = placedCardResponseResource
        )
        private val placedCardMoveResponseJsonChecks =
            listOf(
                "\"responseType\":\"movePlacedCard\"",
                "\"result\":\"success\""
            ) + placedCardResponseJsonChecks
        private val placedCardSelectResponse = PlacedCardSelectResponse(
            requestId = "mockedRequestId",
            placedCard = placedCardResponseResource
        )
        private val placedCardSelectResponseJsonChecks =
            listOf("\"responseType\":\"selectPlacedCard\"") + placedCardResponseJsonChecks

    }

    @ParameterizedTest
    @MethodSource("requestSerializationData")
    fun `PlacedCard requests serialization`(requestType: String, request: IRequest, jsonSubstrings: List<String>) {
        val json = apiV1RequestSerialize(request)
        val jsonAssertions = generateJsonAssertions(jsonSubstrings, json)
        assertJson(requestType, json, jsonAssertions)
    }

    @ParameterizedTest
    @MethodSource("responseSerializationData")
    fun `PlacedCard responses serialization`(responseType: String, response: IResponse, jsonSubstrings: List<String>) {
        val json = apiV1ResponseSerialize(response)
        val jsonAssertions = generateJsonAssertions(jsonSubstrings, json)
        assertJson(responseType, json, jsonAssertions)
    }

    @Test
    fun `PlacedCardCreateRequest deserialization`() {
        val json = """
            {
                "requestType":"createPlacedCard",
                "requestId":"mockedRequestId",
                "debug": {
                    "mode":"test",
                    "stub":"success"
                },
                "placedCard": {
                    "box":"new",
                    "ownerId":"OwnerId",
                    "cardId":"CardId"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as PlacedCardCreateRequest
        assertEquals(placedCardCreateRequest.copy(requestType = "createPlacedCard"), result)
    }

    @Test
    fun `PlacedCardDeleteRequest deserialization`() {
        val json = """
            {
                "requestType":"deletePlacedCard",
                "requestId":"mockedRequestId",
                "debug": { 
                    "mode":"test",
                    "stub":"success"
                },
                "placedCard": {
                    "id":"PlacedCardId",
                    "lock":"LockType"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as PlacedCardDeleteRequest
        assertEquals(placedCardDeleteRequest.copy(requestType = "deletePlacedCard"), result)
    }

    @Test
    fun `PlacedCardInitRequest deserialization`() {
        val json = """
            {
                "requestType":"initPlacedCard",
                "requestId":"mockedRequestId",
                "debug": {
                    "mode":"test",
                    "stub":"success"
                },
                "init": {
                    "ownerId":"OwnerId",
                    "box":"new"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as PlacedCardInitRequest
        assertEquals(placedCardInitRequest.copy(requestType = "initPlacedCard"), result)
    }

    @Test
    fun `PlacedCardMoveRequest deserialization`() {
        val json = """
            {
                "requestType":"movePlacedCard",
                "requestId":"mockedRequestId",
                "debug": {
                    "mode":"test",
                    "stub":"success"
                },
                "move": {
                    "id":"PlacedCardId",
                    "box":"repeat"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as PlacedCardMoveRequest
        assertEquals(placedCardMoveRequest.copy(requestType = "movePlacedCard"), result)
    }

    @Test
    fun `PlacedCardSelectRequest deserialization`() {
        val json = """
            {
                "requestType":"selectPlacedCard",
                "requestId":"mockedRequestId",
                "debug": {
                    "mode":"test",
                    "stub":"success"
                },
                "select": {
                    "ownerId":"OwnerId",
                    "box":"repeat",
                    "searchStrategy":"EarliestCreated"
                }
            }        
        """.trimIndent()
        val result = apiV1RequestDeserialize(json) as PlacedCardSelectRequest
        assertEquals(placedCardSelectRequest.copy(requestType = "selectPlacedCard"), result)
    }

    @Test
    fun `PlacedCardCreateResponse deserialization`() {
        val json = """
            {
                "responseType":"createPlacedCard",
                "requestId":"mockedRequestId",
                "errors": [
                    {
                        "code":"errorCode",
                        "group":"errorGroup",
                        "field":"errorField",
                        "message":"errorMessage"
                    }
                ],
                "placedCard": {
                    "box":"new",
                    "ownerId":"OwnerId",
                    "cardId":"CardId",
                    "id":"PlacedCardId",
                    "createdOn":"2023-09-25T10:00:00Z",
                    "updatedOn":"2023-10-25T10:00:00Z"
                }
            }        
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as PlacedCardCreateResponse
        assertEquals(placedCardCreateResponse.copy(responseType = "createPlacedCard"), result)
    }

    @Test
    fun `PlacedCardDeleteResponse deserialization`() {
        val json = """
            {
                "responseType":"deletePlacedCard",
                "requestId":"mockedRequestId",
                "result":"error"
            }
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as PlacedCardDeleteResponse
        assertEquals(placedCardDeleteResponse.copy(responseType = "deletePlacedCard"), result)
    }

    @Test
    fun `PlacedCardInitResponse deserialization`() {
        val json = """
            {
                "responseType":"initPlacedCard",
                "requestId":"mockedRequestId",
                "result":"success"
            }
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as PlacedCardInitResponse
        assertEquals(placedCardInitResponse.copy(responseType = "initPlacedCard"), result)
    }

    @Test
    fun `PlacedCardMoveResponse deserialization`() {
        val json = """
            {
                "responseType":"movePlacedCard",
                "requestId":"mockedRequestId",
                "result":"success",
                "placedCard": {
                    "box":"new",
                    "ownerId":"OwnerId",
                    "cardId":"CardId",
                    "id":"PlacedCardId",
                    "createdOn":"2023-09-25T10:00:00Z",
                    "updatedOn":"2023-10-25T10:00:00Z"
                }
            }
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as PlacedCardMoveResponse
        assertEquals(placedCardMoveResponse.copy(responseType = "movePlacedCard"), result)
    }

    @Test
    fun `PlacedCardSelectResponse deserialization`() {
        val json = """
            {
                "responseType":"selectPlacedCard",
                "requestId":"mockedRequestId",
                "placedCard": {
                    "box":"new",
                    "ownerId":"OwnerId",
                    "cardId":"CardId",
                    "id":"PlacedCardId",
                    "createdOn":"2023-09-25T10:00:00Z",
                    "updatedOn":"2023-10-25T10:00:00Z"
                }
            }
        """.trimIndent()
        val result = apiV1ResponseDeserialize(json) as PlacedCardSelectResponse
        assertEquals(placedCardSelectResponse.copy(responseType = "selectPlacedCard"), result)
    }
    
    
    
}
