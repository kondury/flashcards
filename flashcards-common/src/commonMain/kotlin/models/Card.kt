package com.github.kondury.flashcards.common.models

data class Card(
    val id: CardId = CardId.NONE,
    val front: String = "",
    val back: String = "",
)
