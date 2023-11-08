package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

fun CorChainDsl<CardContext>.initState(title: String) = worker {
    this.title = title
    activeIf { state == FcState.NONE }
    handle { state = FcState.RUNNING }
}