package com.github.kondury.flashcards.placedcards.app.rabbit.config

data class ProcessorConfig(
    val keyIn: String,
    val keyOut: String,
    val exchange: String,
    val queue: String,
    val consumerTag: String,
    val exchangeType: String = "direct"
)
