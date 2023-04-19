package ru.serj.birds


import kotlinx.coroutines.runBlocking
import ru.serj.birds.kafka.BirdsKafkaJob
import ru.serj.birds.kafka.BirdsKafkaProducer

fun main(): Unit = runBlocking {

    BirdsKafkaJob(
        topic = "birds-tweet-in",
        producer = BirdsKafkaProducer(topic = "birds-tweet-out")
    ).apply {
        start()
    }

}