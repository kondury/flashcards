package com.github.kondury.flashcards.cards.app.kafka

fun main() = with(CardsKafkaConfig()) {
    controller.run()
}
