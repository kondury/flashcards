package com.github.kondury.flashcards.placedcards.common.models

enum class FcSearchStrategy {
    NONE,
    EARLIEST_CREATED,
    EARLIEST_REVIEWED
}

inline fun FcSearchStrategy.isNotEmpty() = this != FcSearchStrategy.NONE