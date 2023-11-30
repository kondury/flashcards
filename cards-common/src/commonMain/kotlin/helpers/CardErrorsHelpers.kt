package com.github.kondury.flashcards.cards.common.helpers

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.exception.OptimisticLockException
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.models.FcError

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

fun CardContext.addError(vararg error: FcError) = errors.addAll(error)

fun repoConcurrencyError(
    expectedLock: FcCardLock,
    actualLock: FcCardLock?,
    exception: Exception = OptimisticLockException(expectedLock, actualLock),
) = FcError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception,
)