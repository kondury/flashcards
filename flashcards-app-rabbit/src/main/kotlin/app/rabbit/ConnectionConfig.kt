package com.github.kondury.flashcards.app.rabbit

import com.rabbitmq.client.ConnectionFactory

data class ConnectionConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String
)

fun ConnectionFactory.configure(config: ConnectionConfig): ConnectionFactory {
    this.host = config.host
    this.port = config.port
    this.username = config.user
    this.password = config.password
    return this
}
