package com.github.kondury.flashcards.cards.mappers.v1

import com.github.kondury.flashcards.cards.api.v1.models.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

internal class ToCardTransportTest {


    companion object {

        private const val CARD_ID = "CardId"
        private const val FRONT_TEXT = "Front text"
        private const val BACK_TEXT = "Back text"

        private const val REQUEST_ID = "RequestId"

        private val cardResponse = Card(
            id = CardId(CARD_ID),
            front = FRONT_TEXT,
            back = BACK_TEXT,
        )

        @JvmStatic
        fun cardAttributesData(): List<Arguments> = listOf(
            Arguments.of(
                CardContext(
                    command = CardCommand.CREATE_CARD,
                    cardResponse = cardResponse,
                ),
                CARD_ID, FRONT_TEXT, BACK_TEXT
            ),
            Arguments.of(
                CardContext(
                    command = CardCommand.READ_CARD,
                    cardResponse = cardResponse,
                ),
                CARD_ID, FRONT_TEXT, BACK_TEXT
            ),
        )

        @JvmStatic
        fun commonAttributesData(): List<Arguments> = listOf(
            Arguments.of(
                CardContext(
                    command = CardCommand.CREATE_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                CardCreateResponse::class,
                ResponseResult.SUCCESS,
                null,
                REQUEST_ID,
            ),
            Arguments.of(
                CardContext(
                    command = CardCommand.READ_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                CardReadResponse::class,
                ResponseResult.SUCCESS,
                null,
                REQUEST_ID,
            ),
            Arguments.of(
                CardContext(
                    command = CardCommand.DELETE_CARD,
                    state = FcState.RUNNING,
                    errors = mutableListOf(
                        FcError("ErrCode", "ErrGroup", "ErrField", "ErrMessage", null)
                    ),
                    requestId = FcRequestId(REQUEST_ID)
                ),
                CardDeleteResponse::class,
                ResponseResult.SUCCESS,
                mutableListOf(
                    Error("ErrCode", "ErrGroup", "ErrField", "ErrMessage")
                ),
                REQUEST_ID,
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("cardAttributesData")
    fun `test create and read card's response attributes mapping`(
        context: CardContext,
        expectedCardId: String,
        expectedFront: String,
        expectedBack: String,
    ) {
        val actualCardResource =
            when (val response = context.toTransportCard()) {
                is CardCreateResponse -> response.card
                is CardReadResponse -> response.card
                else -> fail("Unforeseen response type ${response.javaClass}")
            }

        if (actualCardResource != null)
            with(actualCardResource) {
                assertEquals(expectedCardId, id)
                assertEquals(expectedFront, front)
                assertEquals(expectedBack, back)
            }
        else fail("Actual card resource mustn't be null")
    }

    @ParameterizedTest
    @MethodSource("commonAttributesData")
    fun <T: IResponse> `test common context attributes mapping`(
        context: CardContext,
        expectedResponseClass: KClass<T>,
        expectedResult: ResponseResult,
        expectedErrors: List<Error>?,
        expectedRequestId: String,
    ) {

        val response = context.toTransportCard()

        assertInstanceOf(expectedResponseClass.java, response)
        with(response) {
            assertEquals(expectedRequestId, requestId)
            assertEquals(expectedResult, result)
            assertIterableEquals(expectedErrors, errors)
        }
    }
}