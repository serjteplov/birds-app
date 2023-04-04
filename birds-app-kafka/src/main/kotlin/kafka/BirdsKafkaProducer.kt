package ru.serj.birds.kafka

import mapper
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import ru.serj.api.v1.models.TResponse

class BirdsKafkaProducer(
    private val producer: KafkaProducer<String, String> = KafkaConfiguration().createProducer(),
    private val topic: String
) {

    fun <T : TResponse>send(input: T) {
        val producerRecord = ProducerRecord(
            topic,
            input.requestId,
            mapper.writeValueAsString(input)
        )
        producer.send(producerRecord, callback)
    }
}

val callback: Callback = Callback(
    function = { metadata, exception ->
        if (exception == null) println("Producer send offset: ${metadata.offset()}")
    }
)