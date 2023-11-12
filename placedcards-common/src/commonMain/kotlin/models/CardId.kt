package com.github.kondury.flashcards.placedcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CardId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CardId("")
    }
}

inline fun CardId.isEmpty() = this == CardId.NONE
inline fun CardId.isNotEmpty() = this != CardId.NONE
