package com.github.kondury.flashcards.cards.app.rabbit


fun main(): Unit = with(CardsRabbitConfig()) {
    controller.start()
}
