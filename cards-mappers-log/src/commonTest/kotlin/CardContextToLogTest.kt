package com.github.kondury.flashcards.cards.api.logs.mapper

import com.github.kondury.flashcards.cards.api.logs.models.CardLog
import com.github.kondury.flashcards.cards.api.logs.models.CardLogModel
import com.github.kondury.flashcards.cards.api.logs.models.CardLogModel.Operation
import com.github.kondury.flashcards.cards.api.logs.models.ErrorLogModel
import com.github.kondury.flashcards.cards.api.logs.models.LogModel
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CardContextToLogTest {

    @Test
    fun fromContextToLogModel() = testCardContextToLogMapper(
        cardContext = CardContext(
            command = CardCommand.CREATE_CARD,
            errors = mutableListOf(
                FcError(
                    code = "error-code",
                    field = "error-field",
                    message = "error-message",
                    level = FcError.Level.DEBUG
                )
            ),
            requestId = FcRequestId("req-id"),
            requestCard = Card(CardId("123"), "Request front", "Request back"),
            responseCard = Card(CardId("456"), "Response front", "Response back")
        )
    )
    { log: LogModel ->
        val expectedCardModel = CardLogModel(
            operation = Operation.CREATE,
            requestCard = CardLog("123", "Request front", "Request back"),
            responseCard = CardLog("456", "Response front", "Response back")
        )
        val expectedErrors = listOf(
            ErrorLogModel(
                message = "error-message",
                field = "error-field",
                code = "error-code",
                level = "DEBUG"
            )
        )
        assertEquals("req-id", log.requestId)
        assertEquals(expectedCardModel, log.card)
        assertContentEquals(expectedErrors, log.errors)
    }

    private fun testCardContextToLogMapper(
        cardContext: CardContext, assertSpecific: (log: LogModel) -> Unit
    ) {
        val log = cardContext.toLog("log-id")
        assertEquals("log-id", log.logId)
        assertEquals("flashcards-cards", log.source)
        assertSpecific(log)
    }
}
