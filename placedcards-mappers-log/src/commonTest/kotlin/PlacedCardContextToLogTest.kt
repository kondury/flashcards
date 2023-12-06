package com.github.kondury.flashcards.placedcards.api.logs.mapper

import com.github.kondury.flashcards.placedcards.api.logs.models.PlacedCardLog
import com.github.kondury.flashcards.placedcards.api.logs.models.PlacedCardLogModel
import com.github.kondury.flashcards.placedcards.api.logs.models.PlacedCardLogModel.Operation
import com.github.kondury.flashcards.placedcards.api.logs.models.ErrorLogModel
import com.github.kondury.flashcards.placedcards.api.logs.models.LogModel
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class PlacedCardContextToLogTest {

    companion object {
        private const val REQ_CREATED_AT = "2023-09-09T09:09:09Z"
        private const val REQ_UPDATED_AT = "2023-10-10T10:10:10Z"
        private const val RES_CREATED_AT = "2023-11-11T11:11:11Z"
        private const val RES_UPDATED_AT = "2023-12-12T12:12:12Z"
    }

    @Test
    fun fromContextToLogModel() = testPlacedCardContextToLogMapper(
        placedCardContext = PlacedCardContext(
            command = PlacedCardCommand.MOVE_PLACED_CARD,
            errors = mutableListOf(
                FcError(
                    code = "error-code",
                    field = "error-field",
                    message = "error-message",
                    level = FcError.Level.TRACE
                )
            ),
            requestId = FcRequestId("req-id"),
            requestPlacedCard = PlacedCard(
                id = PlacedCardId("123"),
                ownerId = UserId("user-1"),
                box = FcBox.NEW,
                cardId = CardId("card-1"),
                createdAt = Instant.parse(REQ_CREATED_AT),
                updatedAt = Instant.parse(REQ_UPDATED_AT),
            ),
            requestOwnerId = UserId("user-2"),
            requestWorkBox = FcBox.REPEAT,
            requestSearchStrategy = FcSearchStrategy.EARLIEST_CREATED,
            responsePlacedCard = PlacedCard(
                id = PlacedCardId("456"),
                ownerId = UserId("user-3"),
                box = FcBox.FINISHED,
                cardId = CardId("card-2"),
                createdAt = Instant.parse(RES_CREATED_AT),
                updatedAt = Instant.parse(RES_UPDATED_AT),
            )
        )
    )
    { log: LogModel ->
        val expectedPlacedCardModel = PlacedCardLogModel(
            operation = Operation.MOVE,
            requestPlacedCard = PlacedCardLog(
                id = "123",
                ownerId = "user-1",
                box = "NEW",
                cardId = "card-1",
                createdAt = REQ_CREATED_AT,
                updatedAt = REQ_UPDATED_AT,
            ),
            requestOwnerId = "user-2",
            requestWorkBox = "REPEAT",
            requestSearchStrategy = "EARLIEST_CREATED",
            responsePlacedCard = PlacedCardLog(
                id = "456",
                ownerId = "user-3",
                box = "FINISHED",
                cardId = "card-2",
                createdAt = RES_CREATED_AT,
                updatedAt = RES_UPDATED_AT
            ),
        )
        val expectedErrors = listOf(
            ErrorLogModel(
                message = "error-message",
                field = "error-field",
                code = "error-code",
                level = "TRACE"
            )
        )
        assertEquals("req-id", log.requestId)
        assertEquals(expectedPlacedCardModel, log.placedCard)
        assertContentEquals(expectedErrors, log.errors)
    }

    private fun testPlacedCardContextToLogMapper(
        placedCardContext: PlacedCardContext, assertSpecific: (log: LogModel) -> Unit
    ) {
        val log = placedCardContext.toLog("log-id")
        assertEquals("log-id", log.logId)
        assertEquals("flashcards-placedcards", log.source)
        assertSpecific(log)
    }
}
