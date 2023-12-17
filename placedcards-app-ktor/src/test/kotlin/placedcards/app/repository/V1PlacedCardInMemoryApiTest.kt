package com.github.kondury.flashcards.placedcards.app.repository

import com.github.kondury.flashcards.placedcards.api.v1.models.DebugResource
import com.github.kondury.flashcards.placedcards.api.v1.models.RunMode
import com.github.kondury.flashcards.placedcards.app.V1PlacedCardApiContract
import com.github.kondury.flashcards.placedcards.repository.inmemory.InMemoryPlacedCardRepository


class V1PlacedCardInMemoryApiTest : V1PlacedCardApiContract {

    private val repository = InMemoryPlacedCardRepository(initObjects = initObjects, randomUuid = this::uuid)

    override fun getRepository(test: String) = repository
    override val assertSpecificOn: Boolean = true
    override val debugResource: DebugResource = DebugResource(mode = RunMode.TEST)
}

