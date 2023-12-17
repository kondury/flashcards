package com.github.kondury.flashcards.placedcards.biz

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.chain
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.FcWorkMode
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository

internal fun CorChainDsl<PlacedCardContext>.initState() = worker {
    this.title = "PlacedCardContext state initialization"
    activeIf { state == FcState.NONE }
    handle { state = FcState.RUNNING }
}

internal fun CorChainDsl<PlacedCardContext>.initRepository() = worker {
    this.title = "CardContext repository initialization"
    activeIf { state == FcState.RUNNING }
    handle {
        repository = when (workMode) {
            FcWorkMode.TEST -> repositoryConfig.testRepository
            FcWorkMode.PROD -> repositoryConfig.prodRepository
            else -> PlacedCardRepository.NoOpPlacedCardRepository
        }
        if (workMode != FcWorkMode.STUB && repository == PlacedCardRepository.NoOpPlacedCardRepository)
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

internal fun CorChainDsl<PlacedCardContext>.operation(
    command: PlacedCardCommand,
    block: CorChainDsl<PlacedCardContext>.() -> Unit
) = chain {
    block()
    this.title = "Command ${command.name}"
    activeIf { this.command == command && state == FcState.RUNNING }
}

internal fun CorChainDsl<PlacedCardContext>.finish() = worker {
    this.title = "Switch to Finishing state if it's still Running"
    activeIf { this.workMode != FcWorkMode.STUB && this.state == FcState.RUNNING }
    handle { this.state = FcState.FINISHING }
}