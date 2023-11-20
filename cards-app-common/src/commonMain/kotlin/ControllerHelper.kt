package com.github.kondury.flashcards.cards.app.common

import com.github.kondury.flashcards.cards.api.logs.mapper.toLog
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.biz.fail
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.helpers.asFcError
import com.github.kondury.flashcards.logging.common.AppLogger
import kotlinx.datetime.Clock


suspend inline fun FcCardProcessor.process(
    crossinline receiveThenFromTransport: suspend (CardContext) -> Unit,
    crossinline toTransportThenRespond: suspend (CardContext) -> Unit,
    logger: AppLogger,
    logId: String
) {
    CardContext(timeStart = Clock.System.now()).run {
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