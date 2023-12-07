package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.repository.*
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub

class StubPlacedCardRepository : PlacedCardRepository {
    override suspend fun create(request: PlacedCardDbRequest): PlacedCardDbResponse =
        PlacedCardDbResponse.success(PlacedCardStub.get())

    override suspend fun read(request: PlacedCardIdDbRequest): PlacedCardDbResponse =
        PlacedCardDbResponse.success(PlacedCardStub.get())

    override suspend fun delete(request: PlacedCardIdDbRequest): PlacedCardDbResponse =
        PlacedCardDbResponse.SUCCESS_EMPTY

    override suspend fun move(request: PlacedCardMoveDbRequest): PlacedCardDbResponse =
        PlacedCardDbResponse.success(PlacedCardStub.get())

    override suspend fun select(request: PlacedCardSelectDbRequest): PlacedCardDbResponse =
        PlacedCardDbResponse.success(PlacedCardStub.get())

}