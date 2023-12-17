package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.biz.fail
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse


internal fun PlacedCardContext.postProcess(
    result: PlacedCardDbResponse,
    processSuccessful: PlacedCardContext.() -> Unit,
) {
    if (result.isSuccess) processSuccessful()
    else fail(result.errors)
}

internal fun CorChainDsl<PlacedCardContext>.repositoryResponse(command: PlacedCardCommand) = worker {
    this.title = "Prepare ${command.name} response data for a client"
    activeIf { this.command == command && this.workMode != FcWorkMode.STUB }
    handle { responsePlacedCard = repoResponsePlacedCard }
}