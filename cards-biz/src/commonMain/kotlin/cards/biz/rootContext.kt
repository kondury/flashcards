package com.github.kondury.flashcards.cards.biz

import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.models.FcWorkMode
import com.github.kondury.flashcards.cards.common.permissions.FcUserGroups
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.cor.dsl.worker

internal fun CorChainDsl<CardContext>.initState() = worker {
    this.title = "CardContext state initialization"
    activeIf { state == FcState.NONE }
    handle { state = FcState.RUNNING }
}

internal fun CorChainDsl<CardContext>.initRepository() = worker {
    this.title = "CardContext repository initialization"
    activeIf { state == FcState.RUNNING }
    handle {
        repository = when {
            workMode == FcWorkMode.TEST -> repositoryConfig.testRepository
            workMode == FcWorkMode.PROD -> repositoryConfig.prodRepository
            FcUserGroups.TEST in principal.groups -> repositoryConfig.testRepository
            else -> CardRepository.NoOpCardRepository
        }
        if (workMode != FcWorkMode.STUB && repository == CardRepository.NoOpCardRepository)
            fail(
                configurationError(
                    field = "repository",
                    violationCode = "noDatabaseConfigured",
                    description = "No database is configured for chosen work mode ($workMode). " +
                            "Please, contact the administrator staff"
                )
            )
    }
}

internal fun CorChainDsl<CardContext>.operation(
    command: CardCommand,
    block: CorChainDsl<CardContext>.() -> Unit
) = chain {
    block()
    this.title = "Command ${command.name}"
    activeIf { this.command == command && state == FcState.RUNNING }
}



internal fun CorChainDsl<CardContext>.finish() = worker {
    this.title = "Switch to Finishing state if it's still Running"
    activeIf { this.workMode != FcWorkMode.STUB && this.state == FcState.RUNNING }
    handle { this.state = FcState.FINISHING }
}