package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardIdDbRequest

internal fun CorChainDsl<PlacedCardContext>.repositoryPrepareDelete() = worker {
    this.title = "Prepare to delete card from repository"
    activeIf { state == FcState.RUNNING }
    handle { repoPreparedPlacedCard = repoReadPlacedCard.copy() }
}

internal fun CorChainDsl<PlacedCardContext>.repositoryDelete() = worker {
    this.title = "Remove card from repository by Id"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = PlacedCardIdDbRequest(repoPreparedPlacedCard)
        val result = repository.delete(request)
        postProcess(result) { }
    }
}