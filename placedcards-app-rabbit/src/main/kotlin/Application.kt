package com.github.kondury.flashcards.placedcards.app.rabbit

import com.github.kondury.flashcards.placedcards.app.rabbit.config.AppSettings


fun main(): Unit = with(AppSettings()) {
    controller.start()
}
