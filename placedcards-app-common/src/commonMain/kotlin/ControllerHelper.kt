package com.github.kondury.flashcards.placedcards.app.common

import com.github.kondury.flashcards.logging.common.AppLogger
import com.github.kondury.flashcards.placedcards.api.logs.mapper.toLog
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.biz.fail
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import com.github.kondury.flashcards.placedcards.common.helpers.asFcError
import kotlinx.datetime.Clock


suspend inline fun FcPlacedCardProcessor.process(
    crossinline receiveThenFromTransport: suspend (PlacedCardContext) -> Unit,
    crossinline toTransportThenRespond: suspend (PlacedCardContext) -> Unit,
    logger: AppLogger,
    logId: String
) {
    PlacedCardContext(timeStart = Clock.System.now()).run {
        try {
            logger.withLogging(logId) {
                receiveThenFromTransport(this)
                this@process.exec(this)
                logger.info(
                    msg = "Request $logId processed for ${logger.loggerId}",
                    marker = "BIZ",
                    data = this.toLog(logId)
                )
                toTransportThenRespond(this)
            }
        } catch (e: Throwable) {
            logger.withLogging("$logId-failure") {
                fail(e.asFcError())
                this@process.exec(this)
                toTransportThenRespond(this)
            }
        }
    }
}