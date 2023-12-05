package com.github.kondury.flashcards.cards.app.stubs

import com.github.kondury.flashcards.cards.api.v1.models.DebugResource
import com.github.kondury.flashcards.cards.api.v1.models.DebugStub
import com.github.kondury.flashcards.cards.api.v1.models.RunMode
import com.github.kondury.flashcards.cards.app.V1CardApiContract
import com.github.kondury.flashcards.cards.common.repository.CardRepository

class V1CardStubApiTest : V1CardApiContract {

    override fun getRepository(test: String): CardRepository = CardRepository.NoOpCardRepository

    override val assertSpecificOn: Boolean = false
    override val debugResource: DebugResource = DebugResource(
        mode = RunMode.STUB,
        stub = DebugStub.SUCCESS,
    )
}
