package com.github.kondury.flashcards.cards.app.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import java.util.*

fun KafkaConsumerSettings.createKafkaConsumer(): KafkaConsumer<String, String> =
    Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
        put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer::class.java)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer::class.java)
    }.let { KafkaConsumer(it) }

fun KafkaProducerSettings.createKafkaProducer(): KafkaProducer<String, String> =
    Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts)
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class.java)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer::class.java)
    }.let { KafkaProducer(it) }