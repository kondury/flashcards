package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand

fun CorChainDsl<PlacedCardContext>.initState() = worker {
    this.title = "PlacedCardContext state initialization"
    activeIf { state == FcState.NONE }
    handle { state = FcState.RUNNING }
}

internal fun CorChainDsl<PlacedCardContext>.operation(
    command: PlacedCardCommand,
    block: CorChainDsl<PlacedCardContext>.() -> Unit
) = chain {
    block()
    this.title = "Command ${command.name}"
    activeIf { this.command == command && state == FcState.RUNNING }
}

internal fun CorChainDsl<PlacedCardContext>.finish(command: PlacedCardCommand) = worker {
    this.title = "Finish ${command.name} processing"
    activeIf { this.command == command && this.workMode != FcWorkMode.STUB && this.state == FcState.RUNNING }
    handle { this.state = FcState.FINISHING }
}