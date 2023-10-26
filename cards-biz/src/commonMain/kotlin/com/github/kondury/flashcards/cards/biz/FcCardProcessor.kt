package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand.*
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cards.stubs.CardStub

class FcCardProcessor {
    suspend fun exec(ctx: CardContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == FcWorkMode.STUB) {
            "Currently working only in STUB mode."
        }
        when (ctx.command) {
            CREATE_CARD, READ_CARD -> ctx.responseCard = CardStub.get()
            DELETE_CARD, NONE -> {}
        }
    }
}