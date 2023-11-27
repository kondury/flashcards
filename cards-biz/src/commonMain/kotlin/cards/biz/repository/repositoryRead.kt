package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

internal fun CorChainDsl<CardContext>.repositoryRead() = worker {
    this.title = "Read current card value from repository"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = CardIdDbRequest(validatedCard)
        val result = repository.read(request)
        postProcess(result) {
            requireNotNull(result.data)
            repoReadCard = result.data!!
        }
    }
}

internal fun CorChainDsl<CardContext>.repositoryAfterRead() = worker {
    title = "Prepare repository read response"
    activeIf { state == FcState.RUNNING }
    handle { repoResponseCard = repoReadCard }
}