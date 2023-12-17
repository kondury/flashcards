package com.github.kondury.flashcards.cards.common.repository

import com.github.kondury.flashcards.cards.common.helpers.repoConcurrencyError
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.models.FcError

data class CardDbResponse(
    override val data: Card?,
    override val isSuccess: Boolean,
    override val errors: List<FcError> = emptyList()
) : DbResponse<Card> {

    // todo decide what to do with error/success functions and errors;
    //  whether they are to be moved or let them stay in companion object of CardDbResponse
    companion object {
        val SUCCESS_EMPTY = CardDbResponse(null, true)
        fun success(result: Card) = CardDbResponse(result, true)
        fun error(errors: List<FcError>, data: Card? = null) = CardDbResponse(data, false, errors)
        fun error(error: FcError, data: Card? = null) = CardDbResponse(data, false, listOf(error))
        fun errorConcurrent(expectedLock: FcCardLock, actualCard: Card?): CardDbResponse {
            val actualLock = actualCard?.lock?.let { FcCardLock(it.asString()) }
            return error(repoConcurrencyError(expectedLock, actualLock), actualCard)
        }

        private val emptyIdError = FcError(
            code = "id-empty",
            group = "validation",
            field = "id",
            message = "Id must not be null or blank"
        )

        private val notFoundError = FcError(
            code = "not-found",
            field = "id",
            message = "Not Found"
        )

        private val emptyLockError = FcError(
            code = "lock-empty",
            group = "validation",
            field = "lock",
            message = "Lock must not be null or blank"
        )

        val emptyIdErrorResponse = error(emptyIdError)
        val notFoundErrorResponse = error(notFoundError)
        val emptyLockErrorResponse = error(emptyLockError)
    }
}
