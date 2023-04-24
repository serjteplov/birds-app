import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.serj.dsl.rootChain
import kotlin.test.assertEquals

class DslCorTest {

    @Test
    fun `complex chain example`() = runBlocking {
        // given
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте цепочки статус еще не установлен"
                on { status == CorStatus.NONE }
                handle { status = CorStatus.RUNNING }
                except { status = CorStatus.ERROR }
            }
            chain {
                title = "Последовательная цепочка"
                description = "Последовательный запуск воркеров"
                on { status == CorStatus.RUNNING }
                worker(title = "Лямбда обработчик1") { number += 2 }
                worker(title = "Лямбда обработчик2") { number += 2 }
            }
            parallel {
                title = "Параллельная цепочка"
                description = "Параллельный запуск воркеров"
                on { status == CorStatus.RUNNING && number < 15 }
                worker(title = "worker1") { number++ }
                worker(title = "worker2") { number++ }
            }
            printResult()
        }.build()
        val ctx = TestContext("Unit test", 0, "RUN", CorStatus.NONE)

        // when
        chain.exec(ctx)

        // then
        assertEquals(6, ctx.number)
        assertEquals(CorStatus.RUNNING, ctx.status)
        println("Complete: $ctx")
    }
}