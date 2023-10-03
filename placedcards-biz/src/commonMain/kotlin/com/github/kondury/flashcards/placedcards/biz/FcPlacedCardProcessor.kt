package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub

class FcPlacedCardProcessor {
    suspend fun exec(ctx: PlacedCardContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == FcWorkMode.STUB) {
            "Currently working only in STUB mode."
        }
        when (ctx.command) {
            CREATE_PLACED_CARD,
            MOVE_PLACED_CARD,
            SELECT_PLACED_CARD -> ctx.placedCardResponse = PlacedCardStub.get()
            DELETE_PLACED_CARD,
            INIT_PLACED_CARD,
            NONE -> {}
        }
    }
}