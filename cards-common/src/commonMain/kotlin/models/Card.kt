package com.github.kondury.flashcards.cards.common.models

data class Card(
    val id: CardId = CardId.NONE,
    val front: String = "",
    val back: String = "",
) {
    companion object {
        val EMPTY = Card()
    }
}

fun Card.isEmpty() = this == Card.EMPTY
fun Card.isNotEmpty() = this != Card.EMPTY
