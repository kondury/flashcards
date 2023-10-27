package com.github.kondury.flashcards.app.rabbit

data class ProcessorConfig(
    val keyIn: String,
    val keyOut: String,
    val exchange: String,
    val queue: String,
    val consumerTag: String,
    val exchangeType: String = "direct"
)
