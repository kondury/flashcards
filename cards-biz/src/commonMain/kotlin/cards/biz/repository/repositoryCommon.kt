package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.biz.fail
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker


internal fun CardContext.postProcess(
    result: CardDbResponse,
    processSuccessful: CardContext.() -> Unit,
) {
    if (result.isSuccess) processSuccessful()
    else fail(result.errors)
}

internal fun CorChainDsl<CardContext>.repositoryResponse(command: CardCommand) = worker {
    this.title = "Prepare ${command.name} response data for a client"
    activeIf { this.command == command && this.workMode != FcWorkMode.STUB }
    handle { responseCard = repoResponseCard }
}