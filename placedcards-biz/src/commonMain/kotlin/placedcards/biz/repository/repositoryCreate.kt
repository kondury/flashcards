package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbRequest

internal fun CorChainDsl<PlacedCardContext>.repositoryPrepareCreate() = worker {
    this.title = "Prepare to store data into repository"
    activeIf { state == FcState.RUNNING }
    handle { repoPreparedPlacedCard = validatedPlacedCard.copy() }
}

internal fun CorChainDsl<PlacedCardContext>.repositoryCreate() = worker {
    this.title = "Create new placed card"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = PlacedCardDbRequest(repoPreparedPlacedCard)
        val result = repository.create(request)
        postProcess(result) {
            requireNotNull(result.data)
            repoResponsePlacedCard = result.data!!
        }
    }
}