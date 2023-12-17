package com.github.kondury.flashcards.placedcards.common.repository

import com.github.kondury.flashcards.placedcards.common.models.FcError

interface DbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<FcError>
}
