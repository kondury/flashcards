package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand

internal fun CorChainDsl<PlacedCardContext>.repository(
    command: PlacedCardCommand,
    block: CorChainDsl<PlacedCardContext>.() -> Unit
) = chain {
    block()
    title = "Repository processing for ${command.name}"
    activeIf { state == FcState.RUNNING && workMode != FcWorkMode.STUB }
}

