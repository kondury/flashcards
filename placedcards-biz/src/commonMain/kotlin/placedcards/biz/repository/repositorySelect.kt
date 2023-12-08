package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardSelectDbRequest

internal fun CorChainDsl<PlacedCardContext>.repositorySelect() = worker {
    this.title = "Select placed card from repository"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = PlacedCardSelectDbRequest(
            ownerId = validatedOwnerId,
            box = validatedWorkBox,
            strategy = validatedSearchStrategy,
        )
        val result = repository.select(request)
        postProcess(result) {
            requireNotNull(result.data)
            repoResponsePlacedCard = result.data!!
        }
    }
}
