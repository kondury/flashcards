package com.github.kondury.flashcards.placedcards.common.helpers

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.exception.OptimisticLockException
import com.github.kondury.flashcards.placedcards.common.models.FcError
import com.github.kondury.flashcards.placedcards.common.models.FcPlacedCardLock

fun Throwable.asFcError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message.orEmpty(),
) = FcError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun PlacedCardContext.addError(vararg error: FcError) = errors.addAll(error)

fun repoConcurrencyError(
    expectedLock: FcPlacedCardLock,
    actualLock: FcPlacedCardLock?,
    exception: Exception = OptimisticLockException(expectedLock, actualLock),
) = FcError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception,
)