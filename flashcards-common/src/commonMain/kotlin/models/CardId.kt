package com.github.kondury.flashcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class CardId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CardId("")
    }
}
