package com.github.kondury.flashcards.placedcards.app.helpers

import com.github.kondury.flashcards.logging.common.AppLoggerProvider
import com.github.kondury.flashcards.placedcards.app.common.AuthConfig
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.biz.FcPlacedCardProcessor
import com.github.kondury.flashcards.placedcards.common.PlacedCardRepositoryConfig
import com.github.kondury.flashcards.placedcards.common.PlacedCardsCorConfig
import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardRepository

val authConfig = AuthConfig.TEST

fun testConfig(testRepository: PlacedCardRepository = PlacedCardRepository.NoOpPlacedCardRepository) =
    object : PlacedCardsApplicationConfig {
        override val loggerProvider = AppLoggerProvider()
        override val repositoryConfig = PlacedCardRepositoryConfig(
            prodRepository = PlacedCardRepository.NoOpPlacedCardRepository,
            testRepository = testRepository
        )
        override val corConfig: PlacedCardsCorConfig = PlacedCardsCorConfig(repositoryConfig)
        override val processor: FcPlacedCardProcessor = FcPlacedCardProcessor(corConfig)
        override val auth: AuthConfig = authConfig
    }