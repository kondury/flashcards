package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.cor.dsl.rootChain
import com.github.kondury.flashcards.placedcards.biz.stub.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*

class FcPlacedCardProcessor {

    suspend fun exec(context: PlacedCardContext) = businessChain.exec(context)

    companion object {
        private val businessChain = rootChain<PlacedCardContext> {
            initState()

            operation(CREATE_PLACED_CARD) {
                stubs(CREATE_PLACED_CARD) {
                    stubCreatePlacedCardSuccess()

                    stubValidationWrongOwnerId(CREATE_PLACED_CARD) // empty, not proper format
                    stubValidationWrongCardId(CREATE_PLACED_CARD) // empty, not proper format
                    stubValidationWrongBox(CREATE_PLACED_CARD) // empty

                    stubNotFound(CREATE_PLACED_CARD) // not found ownerId, not found cardId

                    stubDbError(CREATE_PLACED_CARD)
                    stubNoCase(CREATE_PLACED_CARD)
                }
            }

            operation(MOVE_PLACED_CARD) {
                stubMovePlacedCardSuccess()

                stubValidationWrongPlacedCardId(MOVE_PLACED_CARD) // empty, not proper format
                stubValidationWrongBox(MOVE_PLACED_CARD) // empty

                stubNotFound(MOVE_PLACED_CARD) // not found placedCardId

                stubDbError(MOVE_PLACED_CARD)
                stubNoCase(MOVE_PLACED_CARD)
            }

            operation(SELECT_PLACED_CARD) {
                stubSelectPlacedCardSuccess()

                stubValidationWrongOwnerId(SELECT_PLACED_CARD) // empty, not proper format
                stubValidationWrongBox(SELECT_PLACED_CARD) // empty
                stubValidationWrongSearchStrategy(SELECT_PLACED_CARD) // empty

                stubNotFound(SELECT_PLACED_CARD) // not found ownerId, not found placedCard

                stubDbError(SELECT_PLACED_CARD)
                stubNoCase(SELECT_PLACED_CARD)
            }

            operation(DELETE_PLACED_CARD) {
                stubDeletePlacedCardSuccess()

                stubValidationWrongPlacedCardId(DELETE_PLACED_CARD) // empty, not proper format

                stubDbError(DELETE_PLACED_CARD)
                stubNoCase(DELETE_PLACED_CARD)
            }

            operation(INIT_PLACED_CARD) {
                stubInitPlacedCardSuccess()

                stubValidationWrongOwnerId(INIT_PLACED_CARD) // empty, not proper format
                stubValidationWrongBox(INIT_PLACED_CARD) // empty
                stubNotFound(INIT_PLACED_CARD) // not found ownerId

                stubDbError(INIT_PLACED_CARD)
                stubNoCase(INIT_PLACED_CARD)
            }

        }.build()
    }
}
