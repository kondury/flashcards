package com.github.kondury.flashcards.cards.app.common

import com.github.kondury.flashcards.cards.biz.FcCardProcessor
import com.github.kondury.flashcards.cards.common.CardRepositoryConfig
import com.github.kondury.flashcards.cards.common.CardsCorConfig
import com.github.kondury.flashcards.logging.common.AppLoggerProvider

interface CardsApplicationConfig {
    val loggerProvider: AppLoggerProvider
    val repositoryConfig: CardRepositoryConfig
    val corConfig: CardsCorConfig
    val processor: FcCardProcessor
    val auth: AuthConfig
}
