package com.github.kondury.flashcards.app.kafka

fun main() = with(CardsKafkaConfig()) {
    controller.run()
}
