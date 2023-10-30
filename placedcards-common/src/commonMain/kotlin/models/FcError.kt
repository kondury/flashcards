package com.github.kondury.flashcards.placedcards.common.models

data class FcError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
