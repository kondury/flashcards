package com.github.kondury.flashcards.placedcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class FcPlacedCardLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FcPlacedCardLock("")
    }
}

fun FcPlacedCardLock.isEmpty() = this == FcPlacedCardLock.NONE
fun FcPlacedCardLock.isNotEmpty() = this != FcPlacedCardLock.NONE
