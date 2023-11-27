package com.github.kondury.flashcards.cards.biz.repository

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

internal fun CorChainDsl<CardContext>.repositoryPrepareDelete() = worker {
    this.title = "Prepare to delete card from repository"
    activeIf { state == FcState.RUNNING }
    handle { repoPreparedCard = repoReadCard.copy() }
}

internal fun CorChainDsl<CardContext>.repositoryDelete() = worker {
    this.title = "Remove card from repository by Id"
    activeIf { state == FcState.RUNNING }
    handle {
        val request = CardIdDbRequest(repoPreparedCard)
        val result = repository.delete(request)
        postProcess(result) { }
    }
}