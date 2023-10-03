package com.github.kondury.flashcards.cards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class FcRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FcRequestId("")
    }
}
