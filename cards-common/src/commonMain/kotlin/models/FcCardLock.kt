package com.github.kondury.flashcards.cards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class FcCardLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FcCardLock("")
    }
}

fun FcCardLock.isEmpty() = this == FcCardLock.NONE
fun FcCardLock.isNotEmpty() = this != FcCardLock.NONE
