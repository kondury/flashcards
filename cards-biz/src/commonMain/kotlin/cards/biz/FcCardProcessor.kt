package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.biz.stub.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand.*
import com.github.kondury.flashcards.cor.dsl.rootChain


class FcCardProcessor {
    suspend fun exec(ctx: CardContext) = businessChain.exec(ctx)

    companion object {
        private val businessChain = rootChain<CardContext> {
            initState("CardContext status initialization")

            operation(CREATE_CARD) {
                stubs(CREATE_CARD) {
                    stubCreateCardSuccess()
                    stubValidationWrongFrontSide(CREATE_CARD)
                    stubValidationWrongBackSide(CREATE_CARD)
                    stubDbError(CREATE_CARD)
                    stubNoCase(CREATE_CARD)
                }
            }

            operation(READ_CARD) {
                stubs(READ_CARD) {
                    stubReadCardSuccess()
                    stubValidationWrongCardId(READ_CARD)
                    stubNotFound(READ_CARD)
                    stubDbError(READ_CARD)
                    stubNoCase(READ_CARD)
                }
            }

            operation(DELETE_CARD) {
                stubs(DELETE_CARD) {
                    stubDeleteCardSuccess()
                    stubValidationWrongCardId(DELETE_CARD)
                    stubDbError(DELETE_CARD)
                    stubNoCase(DELETE_CARD)
                }
            }

        }.build()
    }
}


