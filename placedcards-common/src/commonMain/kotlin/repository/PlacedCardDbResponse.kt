package com.github.kondury.flashcards.placedcards.common.repository

import com.github.kondury.flashcards.placedcards.common.helpers.repoConcurrencyError
import com.github.kondury.flashcards.placedcards.common.models.FcError
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock
import com.github.kondury.flashcards.placedcards.common.models.PlacedCard


data class PlacedCardDbResponse(
    override val data: PlacedCard?,
    override val isSuccess: Boolean,
    override val errors: List<FcError> = emptyList()
) : DbResponse<PlacedCard> {

    // todo decide what to do with error/success functions and errors;
    //  whether they are to be moved or let them stay in companion object of CardDbResponse
    companion object {
        val SUCCESS_EMPTY = PlacedCardDbResponse(null, true)
        fun success(result: PlacedCard) = PlacedCardDbResponse(result, true)
        fun error(errors: List<FcError>, data: PlacedCard? = null) = PlacedCardDbResponse(data, false, errors)
        fun error(error: FcError, data: PlacedCard? = null) = PlacedCardDbResponse(data, false, listOf(error))
        fun errorConcurrent(expectedLock: FcPlacedCardLock, actualPlacedCard: PlacedCard?): PlacedCardDbResponse {
            val actualLock = actualPlacedCard?.lock?.let { FcPlacedCardLock(it.asString()) }
            return error(repoConcurrencyError(expectedLock, actualLock), actualPlacedCard)
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
