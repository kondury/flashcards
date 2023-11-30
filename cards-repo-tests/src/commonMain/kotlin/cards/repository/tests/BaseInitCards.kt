package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock

abstract class BaseInitCards(private val command: String) : InitObjects<Card> {

    private val stubLock: FcCardLock = FcCardLock("stubLock")

    fun createInitTestModel(
        suffix: String,
        lock: FcCardLock = stubLock,
    ) = Card(
        id = CardId("card-repo-$command-$suffix"),
        front = "$suffix front text",
        back = "$suffix back text",
        lock = lock,
    )
}
