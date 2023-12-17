package com.github.kondury.flashcards.cards.stubs

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.stubs.CardStubSample.CARD_KOTLIN as STUB

object CardStub {
    fun get(): Card = STUB.copy()

    fun getWith(
        id: CardId = STUB.id,
        front: String = STUB.front,
        back: String = STUB.back,
        lock: FcCardLock = STUB.lock,
    ): Card = STUB.copy(id = id, front = front, back = back, lock = lock)
}
