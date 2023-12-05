package com.github.kondury.flashcards.placedcards.app.common

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor

interface PlacedCardsApplicationConfig {
    val processor: FcPlacedCardProcessor
    val loggerProvider: AppLoggerProvider
}


