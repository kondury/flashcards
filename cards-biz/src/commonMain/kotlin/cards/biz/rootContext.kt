package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.cor.dsl.worker

fun CorChainDsl<CardContext>.initState() = worker {
    this.title = "CardContext state initialization"
    activeIf { state == FcState.NONE }
    handle { state = FcState.RUNNING }
}

internal fun CorChainDsl<CardContext>.operation(
    command: CardCommand,
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = "Command ${command.name}"
    activeIf { this.command == command && state == FcState.RUNNING }
}

internal fun CorChainDsl<CardContext>.finish(command: CardCommand) = worker {
    this.title = "Finish ${command.name} processing"
    activeIf { this.command == command && this.workMode != FcWorkMode.STUB && this.state == FcState.RUNNING }
    handle { this.state = FcState.FINISHING }
}