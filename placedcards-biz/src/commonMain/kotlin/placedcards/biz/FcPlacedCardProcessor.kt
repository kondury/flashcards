package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.cor.dsl.rootChain
import com.github.kondury.flashcards.placedcards.biz.permission.accessValidation
import com.github.kondury.flashcards.placedcards.biz.permission.resolvePermissions
import com.github.kondury.flashcards.placedcards.biz.stub.*
import com.github.kondury.flashcards.placedcards.biz.validation.*
import com.github.kondury.flashcards.placedcards.biz.repository.*
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.*

class FcPlacedCardProcessor(
    private val placedCardsCorConfig: PlacedCardsCorConfig
) {
    suspend fun exec(context: PlacedCardContext) =
        businessChain.exec(context.apply { repositoryConfig = placedCardsCorConfig.repositoryConfig })

    companion object {
        private val businessChain = rootChain<PlacedCardContext> {
            initState()
            initRepository()

            operation(CREATE_PLACED_CARD) {
                stubs(CREATE_PLACED_CARD) {
                    stubCreatePlacedCardSuccess()
                    stubValidationWrongOwnerId(CREATE_PLACED_CARD)
                    stubValidationWrongCardId(CREATE_PLACED_CARD)
                    stubValidationWrongBox(CREATE_PLACED_CARD)
                    stubNotFound(CREATE_PLACED_CARD)
                    stubDbError(CREATE_PLACED_CARD)
                    stubNoCase(CREATE_PLACED_CARD)
                }

                validations(CREATE_PLACED_CARD) {
                    beforeCreatePlacedCardValidation()
                    validatePlacedCardIdIsEmpty(CREATE_PLACED_CARD) { validatingPlacedCard.id }
                    validateCardIdIsNotEmpty(CREATE_PLACED_CARD) { validatingPlacedCard.cardId }
                    validateCardIdMatchesFormat(CREATE_PLACED_CARD) { validatingPlacedCard.cardId }
                    validateOwnerIdIsNotEmpty(CREATE_PLACED_CARD) { validatingPlacedCard.ownerId }
                    validateOwnerIdMatchesFormat(CREATE_PLACED_CARD) { validatingPlacedCard.ownerId }
                    validateBoxIsNotEmpty(CREATE_PLACED_CARD) { validatingPlacedCard.box }
                    afterCreatePlacedCardValidation()
                }

                resolvePermissions()
                repository(CREATE_PLACED_CARD) {
                    repositoryPrepareCreate()
                    accessValidation()
                    repositoryCreate()
                    repositoryResponse(CREATE_PLACED_CARD)
                }
            }

            operation(MOVE_PLACED_CARD) {
                stubs(MOVE_PLACED_CARD) {
                    stubMovePlacedCardSuccess()
                    stubValidationWrongPlacedCardId(MOVE_PLACED_CARD)
                    stubValidationWrongBox(MOVE_PLACED_CARD)
                    stubNotFound(MOVE_PLACED_CARD)
                    stubDbError(MOVE_PLACED_CARD)
                    stubNoCase(MOVE_PLACED_CARD)
                }

                validations(MOVE_PLACED_CARD) {
                    beforeMovePlacedCardValidation()
                    validatePlacedCardIdIsNotEmpty(MOVE_PLACED_CARD) { validatingPlacedCard.id }
                    validatePlacedCardIdMatchesFormat(MOVE_PLACED_CARD) { validatingPlacedCard.id }
                    validateLockIsNotEmpty(MOVE_PLACED_CARD)
                    validateLockMatchesFormat(MOVE_PLACED_CARD)
                    validateBoxIsNotEmpty(MOVE_PLACED_CARD) { validatingPlacedCard.box }
                    afterMovePlacedCardValidation()
                }

                resolvePermissions()
                repository(MOVE_PLACED_CARD) {
                    repositoryRead()
                    accessValidation()
                    repositoryPrepareMove()
                    repositoryMove()
                    repositoryResponse(MOVE_PLACED_CARD)
                }
            }

            operation(SELECT_PLACED_CARD) {
                stubs(SELECT_PLACED_CARD) {
                    stubSelectPlacedCardSuccess()
                    stubValidationWrongOwnerId(SELECT_PLACED_CARD)
                    stubValidationWrongBox(SELECT_PLACED_CARD)
                    stubValidationWrongSearchStrategy(SELECT_PLACED_CARD)
                    stubNotFound(SELECT_PLACED_CARD)
                    stubDbError(SELECT_PLACED_CARD)
                    stubNoCase(SELECT_PLACED_CARD)
                }

                validations(SELECT_PLACED_CARD) {
                    beforeSelectPlacedCardValidation()
                    validateOwnerIdIsNotEmpty(SELECT_PLACED_CARD) { validatingOwnerId }
                    validateOwnerIdMatchesFormat(SELECT_PLACED_CARD) { validatingOwnerId }
                    validateBoxIsNotEmpty(SELECT_PLACED_CARD) { validatingWorkBox }
                    validateSearchStrategyIsNotEmpty(SELECT_PLACED_CARD) { validatingSearchStrategy }
                    afterSelectPlacedCardValidation()
                }

                resolvePermissions()
                repository(SELECT_PLACED_CARD) {
                    accessValidation()
                    repositorySelect()
                    repositoryResponse(SELECT_PLACED_CARD)
                }
            }

            operation(DELETE_PLACED_CARD) {
                stubs(DELETE_PLACED_CARD) {
                    stubDeletePlacedCardSuccess()
                    stubValidationWrongPlacedCardId(DELETE_PLACED_CARD)
                    stubDbError(DELETE_PLACED_CARD)
                    stubNoCase(DELETE_PLACED_CARD)
                }

                validations(DELETE_PLACED_CARD) {
                    beforeDeletePlacedCardValidation()
                    validatePlacedCardIdIsNotEmpty(DELETE_PLACED_CARD) { validatingPlacedCard.id }
                    validatePlacedCardIdMatchesFormat(DELETE_PLACED_CARD) { validatingPlacedCard.id }
                    validateLockIsNotEmpty(DELETE_PLACED_CARD)
                    validateLockMatchesFormat(DELETE_PLACED_CARD)
                    afterDeletePlacedCardValidation()
                }

                resolvePermissions()
                repository(DELETE_PLACED_CARD) {
                    repositoryRead()
                    accessValidation()
                    repositoryPrepareDelete()
                    repositoryDelete()
                    repositoryResponse(DELETE_PLACED_CARD)
                }
            }

            operation(INIT_PLACED_CARD) {
                stubs(INIT_PLACED_CARD) {
                    stubInitPlacedCardSuccess()
                    stubValidationWrongOwnerId(INIT_PLACED_CARD)
                    stubValidationWrongBox(INIT_PLACED_CARD)
                    stubNotFound(INIT_PLACED_CARD)
                    stubDbError(INIT_PLACED_CARD)
                    stubNoCase(INIT_PLACED_CARD)
                }

                validations(INIT_PLACED_CARD) {
                    beforeInitPlacedCardValidation()
                    validateOwnerIdIsNotEmpty(INIT_PLACED_CARD) { validatingOwnerId }
                    validateOwnerIdMatchesFormat(INIT_PLACED_CARD) { validatingOwnerId }
                    validateBoxIsNotEmpty(INIT_PLACED_CARD) { validatingWorkBox }
                    afterInitPlacedCardValidation()
                }

//                resolvePermissions()
//                repository(INIT_PLACED_CARD) {
//                    repositoryPrepareInit()
//                    accessValidation()
//                    repositoryInit()
//                    repositoryResponse(INIT_PLACED_CARD)
//                }
            }

            finish()
        }.build()
    }
}
