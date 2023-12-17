package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.biz.repository.*
import com.github.kondury.flashcards.cards.biz.repository.repository
import com.github.kondury.flashcards.cards.biz.stub.*
import com.github.kondury.flashcards.cards.biz.validation.*
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.models.CardCommand.*
import com.github.kondury.flashcards.cor.dsl.rootChain

class FcCardProcessor(
    private val cardsCorConfig: CardsCorConfig
) {
    suspend fun exec(context: CardContext) =
        businessChain.exec(context.apply { repositoryConfig = cardsCorConfig.repositoryConfig })

    companion object {
        private val businessChain = rootChain<CardContext> {
            initState()
            initRepository()

            operation(CREATE_CARD) {
                stubs(CREATE_CARD) {
                    stubCreateCardSuccess()
                    stubValidationWrongCardId(CREATE_CARD)
                    stubValidationWrongFrontSide(CREATE_CARD)
                    stubValidationWrongBackSide(CREATE_CARD)
                    stubDbError(CREATE_CARD)
                    stubNoCase(CREATE_CARD)
                }
                validations(CREATE_CARD) {
                    beforeValidation(CREATE_CARD)
                    validateCardIdIsEmpty()
                    validateFrontIsNotEmpty()
                    validateBackIsNotEmpty()
                    afterValidation(CREATE_CARD)
                }
                repository(CREATE_CARD) {
                    repositoryPrepareCreate()
                    repositoryCreate()
                    repositoryResponse(CREATE_CARD)
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
                validations(READ_CARD) {
                    beforeValidation(READ_CARD)
                    validateCardIdIsNotEmpty()
                    validateCardIdMatchesFormat()
                    afterValidation(READ_CARD)
                }
                repository(READ_CARD) {
                    repositoryRead()
                    repositoryAfterRead()
                    repositoryResponse(READ_CARD)
                }
            }

            operation(DELETE_CARD) {
                stubs(DELETE_CARD) {
                    stubDeleteCardSuccess()
                    stubValidationWrongCardId(DELETE_CARD)
                    stubDbError(DELETE_CARD)
                    stubNoCase(DELETE_CARD)
                }
                validations(DELETE_CARD) {
                    beforeValidation(DELETE_CARD)
                    validateCardIdIsNotEmpty()
                    validateCardIdMatchesFormat()
                    validateLockNotEmpty()
                    validateLockMatchesFormat()
                    afterValidation(DELETE_CARD)
                }
                repository(DELETE_CARD) {
                    repositoryRead()
                    repositoryPrepareDelete()
                    repositoryDelete()
                    repositoryResponse(DELETE_CARD)
                }
            }
            finish()

        }.build()
    }
}
