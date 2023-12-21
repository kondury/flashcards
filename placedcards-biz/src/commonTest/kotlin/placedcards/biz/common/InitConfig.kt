package com.github.kondury.flashcards.placedcards.biz.common

import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.FcError
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import com.github.kondury.flashcards.placedcards.repository.tests.MockPlacedCardRepository
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub


internal fun initProcessor(testRepository: PlacedCardRepository): FcPlacedCardProcessor =
    PlacedCardRepositoryConfig(testRepository = testRepository)
        .let(::PlacedCardsCorConfig)
        .let(::FcPlacedCardProcessor)

internal fun initSingleMockRepository(stub: PlacedCard = PlacedCardStub.get(), newUuid: String = "stub-uuid") =
    MockPlacedCardRepository(
        invokeCreate = {
            PlacedCardDbResponse.success(
                stub.copy(
                    id = PlacedCardId(newUuid),
                    lock = FcPlacedCardLock(newUuid),
                    ownerId = it.placedCard.ownerId,
                    box = it.placedCard.box,
                    cardId = it.placedCard.cardId
                )
            )
        },
        invokeRead = {
            if (it.id == stub.id) PlacedCardDbResponse.success(stub)
            else PlacedCardDbResponse.error(
                FcError(message = "Not found", field = "id")
            )
        },
        invokeDelete = {
            if (it.id == stub.id) PlacedCardDbResponse.SUCCESS_EMPTY
            else PlacedCardDbResponse.error(
                FcError(message = "Not found", field = "id")
            )
        },
        invokeMove = {
            if (it.id == stub.id) PlacedCardDbResponse.success(
                stub.copy(box = it.box, lock = FcPlacedCardLock(newUuid))
            )
            else PlacedCardDbResponse.error(
                FcError(message = "Not found", field = "id")
            )
        },
        invokeSelect = {
            if (it.box == stub.box && it.ownerId == stub.ownerId) PlacedCardDbResponse.success(stub)
            else PlacedCardDbResponse.error(
                FcError(message = "Not found", field = "id")
            )
        }
    )
