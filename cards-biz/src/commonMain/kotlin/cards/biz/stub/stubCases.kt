package com.github.kondury.flashcards.cards.biz.stub

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.stubs.FcStub
import com.github.kondury.flashcards.cards.stubs.CardStub
import com.github.kondury.flashcards.cor.dsl.CorChainDsl


internal fun CorChainDsl<CardContext>.stubCreateCardSuccess() =
    stubSuccess(CardCommand.CREATE_CARD) {
        responseCard = CardStub.getWith(front = requestCard.front, back = requestCard.back)
    }

internal fun CorChainDsl<CardContext>.stubReadCardSuccess() =
    stubSuccess(CardCommand.READ_CARD) {
        responseCard = CardStub.getWith(id = requestCard.id)
    }

internal fun CorChainDsl<CardContext>.stubDeleteCardSuccess() =
    stubSuccess(CardCommand.DELETE_CARD)


internal fun CorChainDsl<CardContext>.stubValidationWrongCardId(command: CardCommand) =
    stubError(
        command,
        FcStub.WRONG_CARD_ID,
        FcError(
            group = "validation",
            code = "validation-id",
            field = "id",
            message = "Wrong id field"
        )
    )


internal fun CorChainDsl<CardContext>.stubValidationWrongFrontSide(command: CardCommand) =
    stubError(
        command,
        FcStub.WRONG_FRONT_SIDE,
        FcError(
            group = "validation",
            code = "validation-front",
            field = "front",
            message = "Wrong front field"
        )
    )


internal fun CorChainDsl<CardContext>.stubValidationWrongBackSide(command: CardCommand) =
    stubError(
        command,
        FcStub.WRONG_BACK_SIDE,
        FcError(
            group = "validation",
            code = "validation-back",
            field = "back",
            message = "Wrong back field"
        )
    )

internal fun CorChainDsl<CardContext>.stubNotFound(command: CardCommand) =
    stubError(
        command,
        FcStub.NOT_FOUND,
        FcError(
            group = "db-error",
            code = "db-error-card-not-found",
            field = "",
            message = "Card is not found"
        )
    )

internal fun CorChainDsl<CardContext>.stubDbError(command: CardCommand) =
    stubError(
        command,
        FcStub.DB_ERROR,
        FcError(
            group = "db-error",
            code = "db-error-general",
            field = "",
            message = "Something has happened with db"
        )
    )

