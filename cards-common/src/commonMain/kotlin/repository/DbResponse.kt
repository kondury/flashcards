package com.github.kondury.flashcards.cards.common.repository

import com.github.kondury.flashcards.cards.common.models.FcError

interface DbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<FcError>
}
