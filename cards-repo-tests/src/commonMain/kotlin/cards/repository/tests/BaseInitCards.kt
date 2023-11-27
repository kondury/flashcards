package com.github.kondury.flashcards.cards.repository.tests

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId

abstract class BaseInitCards(private val command: String): InitObjects<Card> {

    fun createInitTestModel(
        suffix: String,
    ) = Card(
        id = CardId("card-repo-$command-$suffix"),
        front = "$suffix front text",
        back = "$suffix back text"
    )
}
