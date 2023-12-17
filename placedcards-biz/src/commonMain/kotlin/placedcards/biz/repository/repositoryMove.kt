package com.github.kondury.flashcards.placedcards.biz.repository

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardMoveDbRequest

internal fun CorChainDsl<PlacedCardContext>.repositoryPrepareMove() = worker {
    this.title = "Prepare to move card to another box"
    activeIf { state == FcState.RUNNING }
    handle { repoPreparedPlacedCard = repoReadPlacedCard.copy(box = validatedPlacedCard.box) }
}

internal fun CorChainDsl<PlacedCardContext>.repositoryMove() = worker {
    this.title = "Move card into another box"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = PlacedCardMoveDbRequest(repoPreparedPlacedCard)
        val result = repository.move(request)
        postProcess(result) {
            requireNotNull(result.data)
            repoResponsePlacedCard = result.data!!
        }
    }
}