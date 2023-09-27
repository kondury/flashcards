package com.github.kondury.flashcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class PlacedCardId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PlacedCardId("")
    }
}
