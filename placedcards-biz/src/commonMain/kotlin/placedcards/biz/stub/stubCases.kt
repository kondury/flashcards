package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.placedcards.biz.validationError
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub

internal fun CorChainDsl<PlacedCardContext>.stubCreatePlacedCardSuccess() =
    stubSuccess(PlacedCardCommand.CREATE_PLACED_CARD) {
        responsePlacedCard = PlacedCardStub.getWith(
            ownerId = requestPlacedCard.ownerId,
            box = requestPlacedCard.box,
            cardId = requestPlacedCard.cardId,
        )
    }

internal fun CorChainDsl<PlacedCardContext>.stubMovePlacedCardSuccess() =
    stubSuccess(PlacedCardCommand.MOVE_PLACED_CARD) {
        responsePlacedCard = PlacedCardStub.getWith(
            id = requestPlacedCard.id,
            box = requestPlacedCard.box,
        )
    }

internal fun CorChainDsl<PlacedCardContext>.stubSelectPlacedCardSuccess() =
    stubSuccess(PlacedCardCommand.SELECT_PLACED_CARD) {
        responsePlacedCard = PlacedCardStub.getWith(
            ownerId = requestOwnerId,
            box = requestWorkBox,
        )
    }

internal fun CorChainDsl<PlacedCardContext>.stubDeletePlacedCardSuccess() =
    stubSuccess(PlacedCardCommand.DELETE_PLACED_CARD)

internal fun CorChainDsl<PlacedCardContext>.stubInitPlacedCardSuccess() =
    stubSuccess(PlacedCardCommand.INIT_PLACED_CARD)

internal fun CorChainDsl<PlacedCardContext>.stubValidationWrongCardId(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.WRONG_CARD_ID,
        validationError(
            command = command,
            workMode = FcWorkMode.STUB,
            field = "cardId",
            violationCode = "",
            description = "Wrong card id field"
        )
    )

internal fun CorChainDsl<PlacedCardContext>.stubValidationWrongSearchStrategy(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.WRONG_SEARCH_STRATEGY,
        validationError(
            command = command,
            workMode = FcWorkMode.STUB,
            field = "searchStrategy",
            violationCode = "",
            description = "Wrong search strategy field"
        )
    )

internal fun CorChainDsl<PlacedCardContext>.stubValidationWrongPlacedCardId(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.WRONG_PLACED_CARD_ID,
        validationError(
            command = command,
            workMode = FcWorkMode.STUB,
            field = "placedCardId",
            violationCode = "",
            description = "Wrong placed card id field"
        )
    )

internal fun CorChainDsl<PlacedCardContext>.stubValidationWrongBox(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.WRONG_BOX,
        validationError(
            command = command,
            workMode = FcWorkMode.STUB,
            field = "box",
            violationCode = "",
            description = "Wrong box field"
        )
    )

internal fun CorChainDsl<PlacedCardContext>.stubValidationWrongOwnerId(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.WRONG_OWNER_ID,
        validationError(
            command = command,
            workMode = FcWorkMode.STUB,
            field = "ownerId",
            violationCode = "",
            description = "Wrong owner id field"
        )
    )

internal fun CorChainDsl<PlacedCardContext>.stubNotFound(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.NOT_FOUND,
        FcError(
            group = "db-error-stub",
            code = "db-error-stub-not-found",
            field = "",
            message = "not found"
        )
    )

internal fun CorChainDsl<PlacedCardContext>.stubDbError(command: PlacedCardCommand) =
    stubError(
        command,
        FcStub.DB_ERROR,
        FcError(
            group = "db-error-stub",
            code = "db-error-general",
            field = "",
            message = "Something has happened with db"
        )
    )

