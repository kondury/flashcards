package com.github.kondury.flashcards.placedcards.app.kafka

import com.github.kondury.flashcards.app.kafka.AbstractKafkaController
import com.github.kondury.flashcards.app.kafka.TransformationStrategy
import com.github.kondury.flashcards.placedcards.app.common.PlacedCardsApplicationConfig
import com.github.kondury.flashcards.placedcards.app.common.process
import com.github.kondury.flashcards.placedcards.common.PlacedCardContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.Producer

private val loggerId = {}.javaClass.name.substringBefore("Kt$")

class PlacedCardsKafkaController(
    applicationConfig: PlacedCardsApplicationConfig,
    strategies: List<TransformationStrategy<PlacedCardContext>>,
    consumer: Consumer<String, String>,
    producer: Producer<String, String>
) : AbstractKafkaController<PlacedCardContext>(strategies, consumer, producer),
    PlacedCardsApplicationConfig by applicationConfig {

    override suspend fun process(
        strategy: TransformationStrategy<PlacedCardContext>,
        record: ConsumerRecord<String, String>
    ) {
        processor.process(
            { placedCardContext -> strategy.deserialize(record.value(), placedCardContext) },
            { placedCardContext ->
                val json = strategy.serialize(placedCardContext)
                send(strategy.outputTopic, json)
            },
            loggerProvider.logger(loggerId),
            "PlacedCardsKafkaController"
        )
    }
}
