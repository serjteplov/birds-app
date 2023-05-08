package ru.serj.birds.route

import BirdsContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.serj.api.v1.models.*
import ru.serj.biz.processor.BirdsMainProcessor
import toTransport

val processor = BirdsMainProcessor()

suspend fun ApplicationCall.birdsCreate() {
    val req = receive<TweetCreateRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetCreateResponse = birdsContext.toTransport() as TweetCreateResponse
    respond(tweetCreateResponse)
}

suspend fun ApplicationCall.birdsFilter() {
    val req = receive<TweetFilterRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetFilterResponse = birdsContext.toTransport() as TweetFilterResponse
    respond(tweetFilterResponse)
}

suspend fun ApplicationCall.birdsSearch() {
    val req = receive<TweetSearchRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetSearchResponse = birdsContext.toTransport() as TweetSearchResponse
    respond(tweetSearchResponse)
}

suspend fun ApplicationCall.birdsDelete() {
    val req = receive<TweetDeleteRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetDeleteResponse = birdsContext.toTransport() as TweetDeleteResponse
    respond(tweetDeleteResponse)
}