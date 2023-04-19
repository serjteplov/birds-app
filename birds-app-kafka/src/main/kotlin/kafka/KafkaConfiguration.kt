package ru.serj.birds.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class KafkaConfiguration {
    companion object {
        val BIRDS_KAFKA_HOST: String by lazy { System.getenv("BIRDS_KAFKA_HOST") }
        val BIRDS_KAFKA_GROUP_ID: String by lazy { System.getenv("BIRDS_KAFKA_GROUP_ID") }
        val BIRDS_KAFKA_OFFSET_RESET: String by lazy { System.getenv("BIRDS_KAFKA_OFFSET_RESET") }
    }
}

fun KafkaConfiguration.createConsumer(): KafkaConsumer<String, String> {
    val props = Properties()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaConfiguration.BIRDS_KAFKA_HOST
    props[ConsumerConfig.GROUP_ID_CONFIG] = KafkaConfiguration.BIRDS_KAFKA_GROUP_ID
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = KafkaConfiguration.BIRDS_KAFKA_OFFSET_RESET
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
    props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "false"
    props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = "2000"
    props[ConsumerConfig.RETRY_BACKOFF_MS_CONFIG] = "2000"
    return KafkaConsumer<String, String>(props)
}

fun KafkaConfiguration.createProducer(): KafkaProducer<String, String> {
    val props = Properties()
    props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaConfiguration.BIRDS_KAFKA_HOST
    props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    return KafkaProducer<String, String>(props)
}
