package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

internal fun CorChainDsl<CardContext>.repositoryPrepareCreate() = worker {
    this.title = "Prepare to store data into repository"
    activeIf { state == FcState.RUNNING }
    handle { repoPreparedCard = validatedCard.copy() }
}

internal fun CorChainDsl<CardContext>.repositoryCreate() = worker {
    this.title = "Create new card"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = CardDbRequest(repoPreparedCard)
        val result = repository.create(request)
        postProcess(result) {
            requireNotNull(result.data)
            repoResponseCard = result.data!!
        }
    }
}