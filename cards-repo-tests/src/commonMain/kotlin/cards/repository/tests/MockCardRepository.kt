package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.repository.*
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest

class MockCardRepository(
    private val invokeCreate: (CardDbRequest) -> CardDbResponse = { CardDbResponse.SUCCESS_EMPTY },
    private val invokeRead: (CardIdDbRequest) -> CardDbResponse = { CardDbResponse.SUCCESS_EMPTY },
    private val invokeDelete: (CardIdDbRequest) -> CardDbResponse = { CardDbResponse.SUCCESS_EMPTY },
) : CardRepository {
    override suspend fun create(request: CardDbRequest): CardDbResponse = invokeCreate(request)
    override suspend fun read(request: CardIdDbRequest): CardDbResponse = invokeRead(request)
    override suspend fun delete(request: CardIdDbRequest): CardDbResponse = invokeDelete(request)
}
