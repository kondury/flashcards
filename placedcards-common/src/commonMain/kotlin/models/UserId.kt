package com.github.kondury.flashcards.placedcards.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class UserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = UserId("")
    }
}

inline fun UserId.isEmpty() = this == UserId.NONE
inline fun UserId.isNotEmpty() = this != UserId.NONE
