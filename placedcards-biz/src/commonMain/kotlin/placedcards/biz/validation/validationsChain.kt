package com.github.kondury.flashcards.placedcards.biz.validation

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand

fun CorChainDsl<PlacedCardContext>.validations(
    command: PlacedCardCommand,
    block: CorChainDsl<PlacedCardContext>.() -> Unit
) = chain {
    block()
    title = "Validation for ${command.name}"
    activeIf { state == FcState.RUNNING }
}