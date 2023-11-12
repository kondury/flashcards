package com.github.kondury.flashcards.placedcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class FcRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FcRequestId("")
    }
}

inline fun FcRequestId.isNotEmpty() = this != FcRequestId.NONE
