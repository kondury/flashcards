package com.github.kondury.flashcards.cards.app.helpers

import com.github.kondury.flashcards.cards.app.common.AuthConfig
import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.logging.common.AppLoggerProvider


val authConfig = AuthConfig.TEST

fun testConfig(testRepository: CardRepository = CardRepository.NoOpCardRepository) = object : CardsApplicationConfig {
    override val loggerProvider = AppLoggerProvider()
    override val repositoryConfig = CardRepositoryConfig(
        prodRepository = CardRepository.NoOpCardRepository,
        testRepository = testRepository
    )
    override val corConfig: CardsCorConfig = CardsCorConfig(repositoryConfig)
    override val processor: FcCardProcessor = FcCardProcessor(corConfig)
    override val auth: AuthConfig = authConfig
}
