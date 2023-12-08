package com.github.kondury.flashcards.placedcards.common

data class PlacedCardsCorConfig(
    val repositoryConfig: PlacedCardRepositoryConfig = PlacedCardRepositoryConfig.NONE
) {
    companion object {
        val NONE = PlacedCardRepositoryConfig()
    }
}
