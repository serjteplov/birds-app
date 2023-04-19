package ru.serj.birds.route

import BirdsContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import models.BirdsFilterPeriod
import models.BirdsState
import ru.serj.api.v1.models.*
import ru.serj.birds.stub.tweetResponseStub
import toTransport

suspend fun ApplicationCall.birdsCreate() {
    val req = receive<TweetCreateRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    birdsContext.tweetResponse = tweetResponseStub
    birdsContext.state = BirdsState.RUNNING
    val tweetCreateResponse = birdsContext.toTransport() as TweetCreateResponse
    respond(tweetCreateResponse)
}

suspend fun ApplicationCall.birdsFilter() {
    val req = receive<TweetFilterRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    birdsContext.tweetMultiResponse = mutableListOf(tweetResponseStub)
    birdsContext.tweetFilterPeriod = BirdsFilterPeriod(
        from = kotlinx.datetime.Instant.DISTANT_FUTURE,
        to = kotlinx.datetime.Instant.DISTANT_FUTURE
    )
    birdsContext.state = BirdsState.RUNNING
    val tweetFilterResponse = birdsContext.toTransport() as TweetFilterResponse
    respond(tweetFilterResponse)
}

suspend fun ApplicationCall.birdsSearch() {
    val req = receive<TweetSearchRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    birdsContext.tweetMultiResponse = mutableListOf(tweetResponseStub)
    birdsContext.state = BirdsState.RUNNING
    val tweetSearchResponse = birdsContext.toTransport() as TweetSearchResponse
    respond(tweetSearchResponse)
}

suspend fun ApplicationCall.birdsDelete() {
    val req = receive<TweetDeleteRequest>()
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    birdsContext.tweetResponse = tweetResponseStub
    birdsContext.state = BirdsState.RUNNING
    val tweetDeleteResponse = birdsContext.toTransport() as TweetDeleteResponse
    respond(tweetDeleteResponse)
}