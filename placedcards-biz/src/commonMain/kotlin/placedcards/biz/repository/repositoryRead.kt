package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardIdDbRequest

internal fun CorChainDsl<PlacedCardContext>.repositoryRead() = worker {
    this.title = "Read current card value from repository"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = PlacedCardIdDbRequest(validatedPlacedCard)
        val result = repository.read(request)
        postProcess(result) {
            requireNotNull(result.data)
            repoReadPlacedCard = result.data!!
        }
    }
}

//internal fun CorChainDsl<PlacedCardContext>.repositoryAfterRead() = worker {
//    title = "Prepare repository read response"
//    activeIf { state == FcState.RUNNING }
//    handle { repoResponsePlacedCard = repoReadPlacedCard }
//}