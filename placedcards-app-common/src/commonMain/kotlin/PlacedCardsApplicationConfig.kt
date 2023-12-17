package com.github.kondury.flashcards.placedcards.app.common

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig

interface PlacedCardsApplicationConfig {
    val loggerProvider: AppLoggerProvider
    val repositoryConfig: PlacedCardRepositoryConfig
    val corConfig: PlacedCardsCorConfig
    val processor: FcPlacedCardProcessor
}
