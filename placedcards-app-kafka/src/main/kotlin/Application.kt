package com.github.kondury.flashcards.placedcards.app.kafka

fun main() = with(PlacedCardsKafkaConfig()) {
    controller.run()
}
