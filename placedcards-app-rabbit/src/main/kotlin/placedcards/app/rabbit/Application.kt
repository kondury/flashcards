package com.github.kondury.flashcards.placedcards.app.rabbit

fun main(): Unit = with(PlacedCardsRabbitConfig()) {
    controller.start()
}
