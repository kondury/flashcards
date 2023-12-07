package com.github.kondury.flashcards.placedcards.common.repository

interface PlacedCardRepository {
    suspend fun create(request: PlacedCardDbRequest): PlacedCardDbResponse
    suspend fun read(request: PlacedCardIdDbRequest): PlacedCardDbResponse
    suspend fun delete(request: PlacedCardIdDbRequest): PlacedCardDbResponse
    suspend fun move(request: PlacedCardMoveDbRequest): PlacedCardDbResponse
    suspend fun select(request: PlacedCardSelectDbRequest): PlacedCardDbResponse

    companion object {
        val NoOpPlacedCardRepository = object : PlacedCardRepository {
            override suspend fun create(request: PlacedCardDbRequest): PlacedCardDbResponse = noOp()
            override suspend fun read(request: PlacedCardIdDbRequest): PlacedCardDbResponse = noOp()
            override suspend fun delete(request: PlacedCardIdDbRequest): PlacedCardDbResponse = noOp()
            override suspend fun move(request: PlacedCardMoveDbRequest): PlacedCardDbResponse = noOp()
            override suspend fun select(request: PlacedCardSelectDbRequest): PlacedCardDbResponse = noOp()
        }

        private fun noOp(): Nothing {
            error("This method is not supposed to be called.")
        }
    }
}