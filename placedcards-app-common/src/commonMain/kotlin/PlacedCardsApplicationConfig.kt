package com.github.kondury.flashcards.placedcards.app.common

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor

// todo generalize interface and move to flashcards-app-common
// todo extract AppLoggerProvider interface
interface PlacedCardsApplicationConfig {
    val processor: FcPlacedCardProcessor
    val loggerProvider: AppLoggerProvider
}


