package com.github.kondury.flashcards.cards.stubs

import com.github.kondury.flashcards.cards.common.models.*

object CardStubSample {
    val CARD_KOTLIN: Card
        get() = Card(
            id = CardId("100"),
            front = "В каком году вышла версия kotlin 1.0?",
            back = "Официальная версия kotlin 1.0 вышла в 2016 году",
            lock = FcCardLock("stub-lock")
        )
}
