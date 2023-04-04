import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.junit.Test

class BirdsKafkaJobTest {

    private val mockConsumer: MockConsumer<String, String> = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)


    @Test
    fun test1() {




        assert(true)
    }

}