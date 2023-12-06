package com.github.kondury.flashcards.placedcards.common.models

enum class FcSearchStrategy {
    NONE,
    EARLIEST_CREATED,
    EARLIEST_REVIEWED
}

fun FcSearchStrategy.isEmpty() = this == FcSearchStrategy.NONE
fun FcSearchStrategy.isNotEmpty() = this != FcSearchStrategy.NONE