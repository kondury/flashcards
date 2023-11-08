package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain

internal fun CorChainDsl<CardContext>.operation(
    command: CardCommand,
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = "Command ${command.name} processing"
    activeIf { this.command == command && state == FcState.RUNNING }
}

internal fun CorChainDsl<CardContext>.stubs(
    command: CardCommand,
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = "Stubs for ${command.name}"
    activeIf { workMode == FcWorkMode.STUB && state == FcState.RUNNING }
}