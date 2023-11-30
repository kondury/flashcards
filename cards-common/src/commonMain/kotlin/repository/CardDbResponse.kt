package com.github.kondury.flashcards.cards.common.repository

import com.github.kondury.flashcards.cards.common.helpers.repoConcurrencyError
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.models.FcError
import ru.otus.otuskotlin.marketplace.common.repo.DbResponse

data class CardDbResponse(
    override val data: Card?,
    override val isSuccess: Boolean,
    override val errors: List<FcError> = emptyList()
) : DbResponse<Card> {

    companion object {
        val SUCCESS_EMPTY = CardDbResponse(null, true)
        fun success(result: Card) = CardDbResponse(result, true)
        fun error(errors: List<FcError>, data: Card? = null) = CardDbResponse(data, false, errors)
        fun error(error: FcError, data: Card? = null) = CardDbResponse(data, false, listOf(error))
        fun errorConcurrent(expectedLock: FcCardLock, actualCard: Card?): CardDbResponse {
            val actualLock = actualCard?.lock?.let { FcCardLock(it.asString()) }
            return error(repoConcurrencyError(expectedLock, actualLock), actualCard)
        }
    }
}
