package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand

internal fun CorChainDsl<PlacedCardContext>.stubs(
    command: PlacedCardCommand,
    block: CorChainDsl<PlacedCardContext>.() -> Unit
) = chain {
    block()
    this.title = "Stubs for ${command.name}"
    activeIf { workMode == FcWorkMode.STUB && state == FcState.RUNNING }
}