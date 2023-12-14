package com.github.kondury.flashcards.placedcards.app.stubs

import com.github.kondury.flashcards.placedcards.api.v1.models.DebugResource
import com.github.kondury.flashcards.placedcards.api.v1.models.DebugStub
import com.github.kondury.flashcards.placedcards.api.v1.models.RunMode
import com.github.kondury.flashcards.placedcards.app.V1PlacedCardApiContract
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository

class V1PlacedCardStubApiTest : V1PlacedCardApiContract {

    override fun getRepository(test: String): PlacedCardRepository = PlacedCardRepository.NoOpPlacedCardRepository

    override val assertSpecificOn: Boolean = false
    override val debugResource: DebugResource = DebugResource(
        mode = RunMode.STUB,
        stub = DebugStub.SUCCESS,
    )
}
