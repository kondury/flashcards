package com.github.kondury.flashcards.cards.app.repository

import com.github.kondury.flashcards.cards.api.v1.models.DebugResource
import com.github.kondury.flashcards.cards.api.v1.models.RunMode
import com.github.kondury.flashcards.cards.app.V1CardApiContract
import com.github.kondury.flashcards.cards.app.V1CardApiContract.Companion.NEW_UUID
import com.github.kondury.flashcards.cards.app.V1CardApiContract.Companion.stubCard
import com.github.kondury.flashcards.cards.repository.inmemory.InMemoryCardRepository


class V1CardInMemoryApiTest : V1CardApiContract {

    private val repository = InMemoryCardRepository(initObjects = listOf(stubCard), randomUuid = { NEW_UUID })

    override fun getRepository(test: String) = repository
    override val assertSpecificOn: Boolean = true
    override val debugResource: DebugResource = DebugResource(mode = RunMode.TEST)
}

