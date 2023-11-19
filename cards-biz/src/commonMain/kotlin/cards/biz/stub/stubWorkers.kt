package com.github.kondury.flashcards.cards.biz.stub

import com.github.kondury.flashcards.cards.biz.fail
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.CardCommand
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.FcState
import com.github.kondury.flashcards.cards.common.stubs.FcStub
import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker

internal fun CorChainDsl<CardContext>.stubNoCase(command: CardCommand) =
    worker {
        this.title = "Wrong stub case for command ${command.name}"
        activeIf { state == FcState.RUNNING }
        handle {
            fail(
                FcError(
                    group = "stub-error",
                    code = "unsupported-case-stub",
                    field = "stub",
                    message = "Wrong stub case is requested: ${stubCase.name}"
                )
            )
        }
    }

internal fun CorChainDsl<CardContext>.stubSuccess(command: CardCommand, handler: CardContext.() -> Unit = {}) =
    worker {
        title = getStubWorkerTitle(command, FcStub.SUCCESS)
        activeIf { this.state == FcState.RUNNING && this.stubCase == FcStub.SUCCESS }
        handle {
            this.state = FcState.FINISHING
            handler()
        }
    }

internal fun CorChainDsl<CardContext>.stubError(
    command: CardCommand,
    stubCase: FcStub,
    error: FcError,
    handler: CardContext.() -> Unit = {}
) = worker {
    title = getStubWorkerTitle(command, stubCase)
    activeIf { this.stubCase == stubCase && this.state == FcState.RUNNING }
    handle {
        fail(error)
        handler()
    }
}

private fun getStubWorkerTitle(command: CardCommand, stubCase: FcStub): String =
    "Stub command ${command.name} with ${stubCase.name} result"


