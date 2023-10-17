package com.github.kondury.flashcards.cards.app.rabbit.config

import com.rabbitmq.client.ConnectionFactory

data class ConnectionConfig(
    val host: String = HOST,
    val port: Int = PORT,
    val user: String = USER,
    val password: String = PASSWORD
) {
    companion object {
        const val HOST = "localhost"
        const val PORT = 5672
        const val USER = "guest"
        const val PASSWORD = "guest"
    }
}

fun ConnectionFactory.configure(config: ConnectionConfig): ConnectionFactory {
    this.host = config.host
    this.port = config.port
    this.username = config.user
    this.password = config.password
    return this
}
