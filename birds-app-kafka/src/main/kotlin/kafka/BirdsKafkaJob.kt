package ru.serj.birds.kafka

import BirdsContext
import kotlinx.coroutines.*
import mu.KLogger
import mu.KotlinLogging.logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import ru.serj.api.v1.models.TRequest
import ru.serj.birds.mapping.rawToRequest
import strategy.BirdsStrategy
import toTransport
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class BirdsKafkaJob(
    private val topic: String,
    private val producer: BirdsKafkaProducer,
    private val consumer: KafkaConsumer<String, String> = KafkaConfiguration().createConsumer(),
    private val strategies: Map<String, BirdsStrategy> = BirdsStrategy.strategies,
) {

    private val logger: KLogger = logger("BirdsKafkaConsumer $topic")
    private var counter: ConcurrentHashMap<Long, Long> = ConcurrentHashMap()

    suspend fun start() = coroutineScope {
        consumer.use { consumer ->
            consumer.subscribe(listOf(topic))
            while (true) {
                delay(1000)
                logger.info { "trying to poll every 1000 millis" }
                val records = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofMillis(1000))
                }
                for (record in records) {
                    logger.info { ">>>Key: ${record.key()} Value: ${record.value()} Offset: ${record.offset()}" }
                    try {
                        val birdsContext = BirdsContext()
                        val request = rawToRequest<TRequest>(record.value())
                        strategies[request.requestType]?.process(request, birdsContext)
                        val response = birdsContext.toTransport()
                        consumer.commitAsync()
                        producer.send(response)
                    } catch (e: Throwable) {
                        consumer.seek(TopicPartition(topic, record.partition()), record.offset())
                        counter[record.offset()] = counter[record.offset()]?.plus(1L) ?: 1
                        if ((counter[record.offset()]?.compareTo(3L) ?: -1) > 0) {
                            consumer.seek(TopicPartition(topic, record.partition()), record.offset() + 1)
                            consumer.commitAsync()
                            counter.remove(record.offset())
                        }
                    }
                }
            }
        }
    }

    fun stop() {
        consumer.wakeup()
    }
}