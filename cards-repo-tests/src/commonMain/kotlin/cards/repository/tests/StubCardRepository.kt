package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.stubs.CardStub

class StubCardRepository : CardRepository {
    override suspend fun create(request: CardDbRequest) = CardDbResponse.success(CardStub.get())
    override suspend fun read(request: CardIdDbRequest) = CardDbResponse.success(CardStub.get())
    override suspend fun delete(request: CardIdDbRequest) = CardDbResponse.SUCCESS_EMPTY
}