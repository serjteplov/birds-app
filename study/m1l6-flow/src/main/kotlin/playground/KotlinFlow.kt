package playground

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class KotlinFlow {
}

suspend fun main() {
    println("Main is starting")
    delay(3000)
    getUsers().collect() { user -> println(user)}
    println("Main is over")
}

fun getUsers(): Flow<String> = flow {
    val database = listOf("Tom", "Bob", "Sam")  // условная база данных
    var i = 1;
    for (item in database){
        delay(1400L) // имитация продолжительной работы
        println("Emit $i item")
        emit(item) // емитируем значение
        i++
    }
}

//suspend fun awaitCallback(): String = suspendCancellableCoroutine { continuation ->
//    val callback = object : Callback { // Implementation of some callback interface
//        override fun onFailure(call: Call, e: IOException) {
//            TODO("Not yet implemented")
//        }
//        override fun onResponse(call: Call, response: Response) {
//            TODO("Not yet implemented")
//        }
//    }
//    // Register callback with an API
//    api.register(callback)
//    // Remove callback on cancellation
//    continuation.invokeOnCancellation { api.unregister(callback) }
//    // At this point the coroutine is suspended by suspendCancellableCoroutine until callback fires
//}
