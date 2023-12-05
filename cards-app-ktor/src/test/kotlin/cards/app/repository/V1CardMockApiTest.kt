package com.github.kondury.flashcards.cards.app.repository

import com.github.kondury.flashcards.cards.api.v1.models.DebugResource
import com.github.kondury.flashcards.cards.api.v1.models.RunMode
import com.github.kondury.flashcards.cards.app.V1CardApiContract
import com.github.kondury.flashcards.cards.app.V1CardApiContract.Companion.NEW_UUID
import com.github.kondury.flashcards.cards.app.V1CardApiContract.Companion.stubCard
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.tests.MockCardRepository

class V1CardMockApiTest : V1CardApiContract {

    private val repository = MockCardRepository(
        invokeCreate = { CardDbResponse.success(stubCard.copy(id = CardId(NEW_UUID))) },
        invokeRead = { CardDbResponse.success(stubCard.copy(id = it.id)) },
        invokeDelete = { CardDbResponse.SUCCESS_EMPTY }
    )

    override fun getRepository(test: String): CardRepository = repository
    override val assertSpecificOn: Boolean = true
    override val debugResource: DebugResource = DebugResource(mode = RunMode.TEST)
}
