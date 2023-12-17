package com.github.kondury.flashcards.cards.common.repository

interface CardRepository {
    suspend fun create(request: CardDbRequest): CardDbResponse
    suspend fun read(request: CardIdDbRequest): CardDbResponse
    suspend fun delete(request: CardIdDbRequest): CardDbResponse

    companion object {
        val NoOpCardRepository = object : CardRepository {
            override suspend fun create(request: CardDbRequest): CardDbResponse = noOp()
            override suspend fun read(request: CardIdDbRequest): CardDbResponse = noOp()
            override suspend fun delete(request: CardIdDbRequest): CardDbResponse = noOp()
        }

        private fun noOp(): Nothing {
            error("This method is not supposed to be called.")
        }
    }
}