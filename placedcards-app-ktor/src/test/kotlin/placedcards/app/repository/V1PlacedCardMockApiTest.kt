package com.github.kondury.flashcards.placedcards.app.repository

import com.github.kondury.flashcards.placedcards.api.v1.models.DebugResource
import com.github.kondury.flashcards.placedcards.api.v1.models.RunMode
import com.github.kondury.flashcards.placedcards.app.V1PlacedCardApiContract
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository
import com.github.kondury.flashcards.placedcards.repository.tests.MockPlacedCardRepository
import kotlinx.datetime.Clock

class V1PlacedCardMockApiTest : V1PlacedCardApiContract {

    private val repository = MockPlacedCardRepository(
        invokeCreate = {
            val timestamp = Clock.System.now()
            PlacedCardDbResponse.success(
                PlacedCard(
                    id = PlacedCardId(uuid),
                    lock = FcPlacedCardLock(uuid),
                    box = it.placedCard.box,
                    ownerId = it.placedCard.ownerId,
                    cardId = it.placedCard.cardId,
                    createdAt = timestamp,
                    updatedAt = timestamp,
                )
            )
        },
        invokeRead = { PlacedCardDbResponse.success(placedCardStub.copy(id = it.id)) },
        invokeDelete = { PlacedCardDbResponse.SUCCESS_EMPTY },
        invokeMove = {
            PlacedCardDbResponse.success(
                placedCardStub.copy(
                    box = it.box,
                    lock = FcPlacedCardLock(uuid),
                    updatedAt = Clock.System.now()
                )
            )
        },
        invokeSelect = { PlacedCardDbResponse.success(placedCardStub) },
    )

    override fun getRepository(test: String): PlacedCardRepository = repository
    override val assertSpecificOn: Boolean = true
    override val debugResource: DebugResource = DebugResource(mode = RunMode.TEST)
}
