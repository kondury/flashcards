package com.github.kondury.flashcards.cards.biz.validation

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

fun CorChainDsl<CardContext>.afterValidation(command: CardCommand) = worker {
    this.title = "Prepare context after successful validation of ${command.name} params"
    activeIf { state == FcState.RUNNING }
    handle {
        validatedCard = validatingCard
    }
}