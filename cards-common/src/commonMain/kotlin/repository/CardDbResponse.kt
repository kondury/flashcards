package com.github.kondury.flashcards.cards.common.repository

import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.FcError
import ru.otus.otuskotlin.marketplace.common.repo.DbResponse

data class CardDbResponse(
    override val data: Card?,
    override val isSuccess: Boolean,
    override val errors: List<FcError> = emptyList()
): DbResponse<Card> {

    companion object {
        val SUCCESS_EMPTY = CardDbResponse(null, true)
        fun success(result: Card) = CardDbResponse(result, true)
        fun error(errors: List<FcError>) = CardDbResponse(null, false, errors)
        fun error(error: FcError) = CardDbResponse(null, false, listOf(error))
    }
}
