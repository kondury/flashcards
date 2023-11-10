package com.github.kondury.flashcards.placedcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class PlacedCardId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PlacedCardId("")
    }
}

fun PlacedCardId.isEmpty() = this == PlacedCardId.NONE
fun PlacedCardId.isNotEmpty() = this != PlacedCardId.NONE
