package com.github.kondury.flashcards.cards.app.common

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.logging.common.AppLoggerProvider

interface CardsApplicationConfig {
    val processor: FcCardProcessor
    val loggerProvider: AppLoggerProvider
}
