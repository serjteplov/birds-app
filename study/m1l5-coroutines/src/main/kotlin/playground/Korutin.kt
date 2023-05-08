package playground

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Korutin {


}


suspend fun main() = runBlocking {
    println("1 I'm working in thread ${Thread.currentThread().name}")
    Thread.sleep(1000)
    println("2")
    Thread.sleep(1000)
    println("3")

    // выполнится параллельно выполнению функции main
    launch(Dispatchers.IO) {
        println("launch1 I'm working in thread ${Thread.currentThread().name}")
        println("launch2")
        Thread.sleep(5000)
        println("launch3")
        println("launch4 I'm working in thread ${Thread.currentThread().name}")
    }
    // выполнится последовательно c выполнением функции main
    coroutineScope {
        launch { println("I'm working in thread ${Thread.currentThread().name}") }
    }

    Thread.sleep(1000)
    println("4")
    println("5")
    println("6 I'm working in thread ${Thread.currentThread().name}")
}
