package homework

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.system.measureTimeMillis

class MeaningService {

    var dictionaries: List<Dictionary> = mutableListOf()
    val httpClient: OkHttpClient = OkHttpClient.Builder().cache(null).build()
    val baseUrl: String = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    val objectMapper: ObjectMapper = ObjectMapper()
    fun read() = javaClass.getResource("/words.txt")
        ?.readText()
        ?.split(" ")
        ?.distinct()

    private fun getSynonyms(it: String): MutableList<String>? {
        return runCatching {
            val request = Request.Builder().url("$baseUrl$it").build()
            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string()
            val jsonNode = objectMapper.readTree(responseBody)
            val jsonNode1 = jsonNode[0].get("meanings")
            val mutableList = mutableListOf<String>()
            if (jsonNode1.isArray) {
                jsonNode1.forEach { j1 ->
                    val jsonNode2 = j1.get("synonyms")
                    if (jsonNode2.isArray) {
                        jsonNode2.forEach { j2 ->
                            mutableList.add(j2.asText())
                        }
                    }
                }
            }
            mutableList
        }.getOrNull()
    }

    fun findWordsSync(spisok: List<String>?) = spisok?.map {
        val synonyms = getSynonyms(it)
        Dictionary(it, synonyms?.toTypedArray())
    } ?: emptyList()

    suspend fun findWords(spisok: List<String>?) = coroutineScope {
        return@coroutineScope spisok?.map {
            async(Dispatchers.Default) {
                val synonyms = getSynonyms(it)
                Dictionary(it, synonyms?.toTypedArray())
            }
        } ?: emptyList()
    }
}

fun main() = runBlocking {
    val meaningService = MeaningService()
    println(meaningService.dictionaries)
    val slova = meaningService.read()


    val timeAsync = measureTimeMillis {
        val async = meaningService.findWords(slova).map { it.await() }
    }


    val timeSync = measureTimeMillis {
        val synched = meaningService.findWordsSync(slova)
    }


    println("execution time (async) = $timeAsync")
    println("execution time (sync) = $timeSync")
}
