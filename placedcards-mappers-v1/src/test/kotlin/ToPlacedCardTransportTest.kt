package com.github.kondury.flashcards.placedcards.mappers.v1

import com.github.kondury.flashcards.placedcards.api.v1.models.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass


internal class ToPlacedCardTransportTest {

    companion object {

        private const val PLACED_CARD_ID = "PlacedCardId"
        private const val CARD_ID = "CardId"
        private const val USER_ID = "UserId"
        private const val CREATED_AT = "2023-09-09T09:09:09Z"
        private const val UPDATED_AT = "2023-10-10T10:10:10Z"
        private const val LOCK = "123-234-abc-ABC"

        private const val REQUEST_ID = "RequestId"

        private val placedCardResponse = PlacedCard(
            id = PlacedCardId(PLACED_CARD_ID),
            lock = FcPlacedCardLock(LOCK),
            ownerId = UserId(USER_ID),
            cardId = CardId(CARD_ID),
            createdAt = Instant.parse(CREATED_AT),
            updatedAt = Instant.parse(UPDATED_AT),
        )

        @JvmStatic
        fun placedCardAttributesData(): List<Arguments> = listOf(
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.CREATE_PLACED_CARD,
                    responsePlacedCard = placedCardResponse.copy(box = FcBox.NEW),
                ),
                PLACED_CARD_ID, USER_ID, Box.NEW, CARD_ID, CREATED_AT, UPDATED_AT, LOCK
            ),
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.MOVE_PLACED_CARD,
                    responsePlacedCard = placedCardResponse.copy(box = FcBox.FINISHED),
                ),
                PLACED_CARD_ID, USER_ID, Box.FINISHED, CARD_ID, CREATED_AT, UPDATED_AT, LOCK
            ),
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.SELECT_PLACED_CARD,
                    responsePlacedCard = placedCardResponse.copy(box = FcBox.REPEAT),
                ),
                PLACED_CARD_ID, USER_ID, Box.REPEAT, CARD_ID, CREATED_AT, UPDATED_AT, LOCK
            ),
        )

        @JvmStatic
        fun commonAttributesData(): List<Arguments> = listOf(
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.CREATE_PLACED_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                PlacedCardCreateResponse::class,
                ResponseResult.SUCCESS,
                null,
                REQUEST_ID,
            ),
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.MOVE_PLACED_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                PlacedCardMoveResponse::class,
                ResponseResult.SUCCESS,
                null,
                REQUEST_ID,
            ),
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.DELETE_PLACED_CARD,
                    state = FcState.FAILING,
                    errors = mutableListOf(
                        FcError("ErrCode", "ErrGroup", "ErrField", "ErrMessage", null)
                    ),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                PlacedCardDeleteResponse::class,
                ResponseResult.ERROR,
                mutableListOf(
                    Error("ErrCode", "ErrGroup", "ErrField", "ErrMessage")
                ),
                REQUEST_ID,
            ),
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.INIT_PLACED_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                PlacedCardInitResponse::class,
                ResponseResult.SUCCESS,
                null,
                REQUEST_ID,
            ),
            Arguments.of(
                PlacedCardContext(
                    command = PlacedCardCommand.SELECT_PLACED_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                PlacedCardSelectResponse::class,
                ResponseResult.SUCCESS,
                null,
                REQUEST_ID,
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("placedCardAttributesData")
    fun `test create, move and select placed card's response attributes mapping`(
        context: PlacedCardContext,
        expectedPlacedCardId: String,
        expectedUserId: String,
        expectedBox: Box,
        expectedCardId: String,
        expectedCreatedOn: String,
        expectedUpdatedOn: String,
        expectedLock: String
    ) {
        val actualPlacedCardResource =
            when (val response = context.toTransportPlacedCard()) {
                is PlacedCardMoveResponse -> response.placedCard
                is PlacedCardCreateResponse -> response.placedCard
                is PlacedCardSelectResponse -> response.placedCard
                else -> fail("Unforeseen response type ${response.javaClass}")
            }

        if (actualPlacedCardResource != null)
            with(actualPlacedCardResource) {
                assertEquals(expectedPlacedCardId, id)
                assertEquals(expectedCardId, cardId)
                assertEquals(expectedUserId, ownerId)
                assertEquals(expectedBox, box)
                assertEquals(expectedCreatedOn, createdAt)
                assertEquals(expectedUpdatedOn, updatedAt)
                assertEquals(expectedLock, lock)
            }
        else fail("Actual placed card resource mustn't be null")
    }

    @ParameterizedTest
    @MethodSource("commonAttributesData")
    fun `test common context attributes mapping`(
        context: PlacedCardContext,
        expectedResponseClass: KClass<IResponse>,
        expectedResult: ResponseResult,
        expectedErrors: List<Error>?,
        expectedRequestId: String,
    ) {

        val response = context.toTransportPlacedCard()

        assertInstanceOf(expectedResponseClass.java, response)
        with(response) {
            assertEquals(expectedRequestId, requestId)
            assertEquals(expectedResult, result)
            assertIterableEquals(expectedErrors, errors)
        }
    }
}