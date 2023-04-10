import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.serj.birds.kafka.BirdsKafkaJob
import ru.serj.birds.kafka.BirdsKafkaProducer
import kotlin.test.assertEquals

class BirdsKafkaJobTest {

    private val mockConsumer: MockConsumer<String, String> = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
    private val mockProducer: MockProducer<String, String> = MockProducer(true, StringSerializer(), StringSerializer())
    private val job = BirdsKafkaJob(
        topic = "birds-tweet-in",
        producer = BirdsKafkaProducer(producer = mockProducer, topic = "birds-tweet-out"),
        consumer = mockConsumer
    )

    @Test
    fun test1() = runBlocking {

        // given
        val tp = TopicPartition("birds-tweet-in", 1)
        mockConsumer.schedulePollTask {
            mockConsumer.rebalance(listOf(tp))
            mockConsumer.addRecord(
                ConsumerRecord(
                    "birds-tweet-in",
                    1,
                    1,
                    "someKey",
                    "{\"requestType\":\"search\",\"requestId\":\"requestId\",\"tweetFilter\":{\"searchString\":\"google\"}}"
                )
            )
        }
        mockConsumer.schedulePollTask { job.stop() }

        val startOffsets = HashMap<TopicPartition, Long>()
        startOffsets[tp] = 0
        mockConsumer.updateBeginningOffsets(startOffsets)

        // when
        job.start()

        // then
        assert(mockConsumer.closed())
        assertEquals(1, mockProducer.history().size)
        assertEquals("requestId", mockProducer.history()[0].key())
        assertEquals(
            "{\"responseType\":\"search\",\"requestId\":\"requestId\",\"result\":\"success\"}",
            mockProducer.history()[0].value()
        )
    }

}