package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcState

internal fun CardContext.fail(error: FcError) {
    state = FcState.FAILING
    errors.add(error)
}