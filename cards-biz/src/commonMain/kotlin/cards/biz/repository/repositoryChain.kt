package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain

internal fun CorChainDsl<CardContext>.repository(
    command: CardCommand,
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    title = "Repository processing for ${command.name}"
    activeIf { state == FcState.RUNNING && workMode != FcWorkMode.STUB }
}

