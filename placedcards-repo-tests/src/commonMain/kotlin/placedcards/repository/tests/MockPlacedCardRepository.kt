package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.repository.*
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardIdDbRequest

class MockPlacedCardRepository(
    private val invokeCreate: (PlacedCardDbRequest) -> PlacedCardDbResponse = { PlacedCardDbResponse.SUCCESS_EMPTY },
    private val invokeRead: (PlacedCardIdDbRequest) -> PlacedCardDbResponse = { PlacedCardDbResponse.SUCCESS_EMPTY },
    private val invokeDelete: (PlacedCardIdDbRequest) -> PlacedCardDbResponse = { PlacedCardDbResponse.SUCCESS_EMPTY },
    private val invokeMove: (PlacedCardMoveDbRequest) -> PlacedCardDbResponse = { PlacedCardDbResponse.SUCCESS_EMPTY },
    private val invokeSelect: (PlacedCardSelectDbRequest) -> PlacedCardDbResponse = { PlacedCardDbResponse.SUCCESS_EMPTY },
) : PlacedCardRepository {
    override suspend fun create(request: PlacedCardDbRequest): PlacedCardDbResponse = invokeCreate(request)
    override suspend fun read(request: PlacedCardIdDbRequest): PlacedCardDbResponse = invokeRead(request)
    override suspend fun delete(request: PlacedCardIdDbRequest): PlacedCardDbResponse = invokeDelete(request)
    override suspend fun move(request: PlacedCardMoveDbRequest): PlacedCardDbResponse = invokeMove(request)
    override suspend fun select(request: PlacedCardSelectDbRequest): PlacedCardDbResponse = invokeSelect(request)
}
