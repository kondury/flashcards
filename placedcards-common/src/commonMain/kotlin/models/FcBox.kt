package com.github.kondury.flashcards.placedcards.common.models

enum class FcBox {
    NONE,
    NEW,
    REPEAT,
    FINISHED
}

fun FcBox.isEmpty() = this == FcBox.NONE
fun FcBox.isNotEmpty() = this != FcBox.NONE