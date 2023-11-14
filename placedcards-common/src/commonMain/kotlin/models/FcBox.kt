package com.github.kondury.flashcards.placedcards.common.models

enum class FcBox {
    NONE,
    NEW,
    REPEAT,
    FINISHED
}

inline fun FcBox.isEmpty() = this == FcBox.NONE
inline fun FcBox.isNotEmpty() = this != FcBox.NONE