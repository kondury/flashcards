package com.github.kondury.flashcards.common.models

enum class PlacedCardCommand : FcCommand {
    NONE,
    CREATE_PLACED_CARD,
    MOVE_PLACED_CARD,
    DELETE_PLACED_CARD,
    SELECT_PLACED_CARD,
    INIT_PLACED_CARD,
}
