package com.github.kondury.flashcards.placedcards.biz.stub

import com.github.kondury.flashcards.cor.dsl.CorChainDsl
import com.github.kondury.flashcards.cor.dsl.worker
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.fail
import com.github.kondury.flashcards.placedcards.common.models.FcError
import com.github.kondury.flashcards.placedcards.common.models.FcState
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand
import com.github.kondury.flashcards.placedcards.common.stubs.FcStub

internal fun CorChainDsl<PlacedCardContext>.stubNoCase(command: PlacedCardCommand) =
    worker {
        this.title = "Wrong stub case for command ${command.name}"
        activeIf { state == FcState.RUNNING }
        handle {
            fail(
                FcError(
                    group = "error-stub",
                    code = "unsupported-case-stub",
                    field = "stub",
                    message = "Wrong stub case is requested: ${stubCase.name}"
                )
            )
        }
    }

internal fun CorChainDsl<PlacedCardContext>.stubSuccess(
    command: PlacedCardCommand,
    handler: PlacedCardContext.() -> Unit = {}
) =
    worker {
        title = getStubWorkerTitle(command, FcStub.SUCCESS)
        activeIf { this.state == FcState.RUNNING && this.stubCase == FcStub.SUCCESS }
        handle {
            this.state = FcState.FINISHING
            handler()
        }
    }

internal fun CorChainDsl<PlacedCardContext>.stubError(
    command: PlacedCardCommand,
    stubCase: FcStub,
    error: FcError,
    handler: PlacedCardContext.() -> Unit = {}
) = worker {
    title = getStubWorkerTitle(command, stubCase)
    activeIf { this.stubCase == stubCase && this.state == FcState.RUNNING }
    handle {
        fail(error)
        handler()
    }
}

private fun getStubWorkerTitle(command: PlacedCardCommand, stubCase: FcStub): String =
    "Stub command ${command.name} with ${stubCase.name} result"


