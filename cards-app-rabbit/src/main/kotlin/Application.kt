package com.github.kondury.flashcards.cards.app.rabbit

import com.github.kondury.flashcards.cards.app.rabbit.config.AppSettings


fun main(): Unit = with(AppSettings()) {
    controller.use { it.start() }
}
