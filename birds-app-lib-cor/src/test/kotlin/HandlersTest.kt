import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.serj.handlers.Chain
import ru.serj.handlers.Worker
import ru.serj.handlers.executeSequential

class HandlersTest {


    @Test
    fun `worker should work`() = runBlocking {

        val worker: Worker<TestContext> = Worker(
            title1 = "title",
            description1 = "description",
            on1 = { name == "fuuu" },
            handle1 = {
                phase += "___"
                number += 10
            },
            except1 = {
                println(it.message)
            }

        )

        val ctx = TestContext("fuuu", 42, "start")
        worker.handle1(ctx)

        assert(ctx.phase == "start___")
        assert(ctx.number == 52)
    }

    @Test
    fun `test chain`() = runBlocking {
        val ctx = TestContext("fuuu", 42, "start")

        val createWorker = { inp: String ->
            Worker<TestContext>(
                title1 = inp,
                description1 = "description",
                on1 = { name == "fuuu" },
                handle1 = {
                    phase += "___"
                    number += 10
                }
            )
        }
        val worker1 = createWorker("w1")
        val worker2 = createWorker("w2")
        val chain = Chain(
            execs = listOf(worker1, worker2),
            title = "chain",
            description = "descr",
            handler = { context, workers -> executeSequential(context, workers) }
        )
        chain.exec(ctx)
        assert(ctx.phase == "start______")
        assert(ctx.number == 62)
    }

}