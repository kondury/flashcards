package com.github.kondury.flashcards.cards.biz.validation

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

internal fun CorChainDsl<CardContext>.beforeValidation(command: CardCommand) = worker {
    this.title = "Prepare context before validation of ${command.name} params"
    activeIf { state == FcState.RUNNING }
    handle { validatingCard = requestCard.normalized }
}

private val Card.normalized: Card
    get() = Card(
        id = id.normalized,
        front = front.normalized,
        back = back.normalized
    )

private val CardId.normalized: CardId
    get() = CardId(asString().normalized)

private val String.normalized: String
    get() = trim()



