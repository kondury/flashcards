package com.github.kondury.flashcards.cards.biz.common

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.tests.MockCardRepository
import com.github.kondury.flashcards.cards.stubs.CardStub


internal fun initProcessor(testRepository: CardRepository): FcCardProcessor =
    CardRepositoryConfig(testRepository = testRepository)
        .let(::CardsCorConfig)
        .let(::FcCardProcessor)

internal fun initSingleMockRepository(stub: Card = CardStub.get(), newUuid: String = "stub-uuid") =
    MockCardRepository(
        invokeCreate = {
            CardDbResponse.success(
                Card(CardId(newUuid), it.card.front, it.card.back)
            )
        },
        invokeRead = {
            if (it.id == stub.id) CardDbResponse.success(stub)
            else CardDbResponse.error(
                FcError(message = "Not found", field = "id")
            )
        },
        invokeDelete = {
            if (it.id == stub.id) CardDbResponse.SUCCESS_EMPTY
            else CardDbResponse.error(
                FcError(message = "Not found", field = "id")
            )
        }
    )

