package com.github.kondury.flashcards.cards.app.kafka

import com.github.kondury.flashcards.app.kafka.AbstractKafkaController
import com.github.kondury.flashcards.app.kafka.TransformationStrategy
import com.github.kondury.flashcards.cards.app.common.CardsApplicationConfig
import com.github.kondury.flashcards.cards.app.common.process
import com.github.kondury.flashcards.cards.common.CardContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.Producer

private val loggerId = {}.javaClass.name.substringBefore("Kt$")

class CardsKafkaController(
    applicationConfig: CardsApplicationConfig,
    strategies: List<TransformationStrategy<CardContext>>,
    consumer: Consumer<String, String>,
    producer: Producer<String, String>
) : AbstractKafkaController<CardContext>(strategies, consumer, producer),
    CardsApplicationConfig by applicationConfig {

    override suspend fun process(
        strategy: TransformationStrategy<CardContext>,
        record: ConsumerRecord<String, String>
    ) {
        processor.process(
            { cardContext ->
                strategy.deserialize(record.value(), cardContext)
            },
            { cardContext ->
                val json = strategy.serialize(cardContext)
                send(strategy.outputTopic, json)
            },
            loggerProvider.logger(loggerId),
            "CardsKafkaController"
        )
    }
}
