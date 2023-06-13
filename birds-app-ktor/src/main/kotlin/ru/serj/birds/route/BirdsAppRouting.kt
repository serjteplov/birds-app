package ru.serj.birds.route

import BirdsContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.serj.api.v1.models.*
import ru.serj.birds.settings.AppSettings
import toTransport


suspend fun ApplicationCall.birdsCreate(settings: AppSettings) {
    val logger = settings.logSettings.loggerProvider.getLogger("Прокинул!!!")
    val processor = settings.corSettings
    logger.doWithLogging {
        val req = receive<TweetCreateRequest>()
        val birdsContext = BirdsContext()
        birdsContext.fromTransport(req)
        logger.info(msg = "this is data", data = birdsContext)
        processor.process(birdsContext)
        val tweetCreateResponse = birdsContext.toTransport() as TweetCreateResponse
        respond(tweetCreateResponse)
    }
}

suspend fun ApplicationCall.birdsFilter(settings: AppSettings) {
    val req = receive<TweetFilterRequest>()
    val processor = settings.corSettings
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetFilterResponse = birdsContext.toTransport() as TweetFilterResponse
    respond(tweetFilterResponse)
}

suspend fun ApplicationCall.birdsSearch(settings: AppSettings) {
    val req = receive<TweetSearchRequest>()
    val processor = settings.corSettings
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetSearchResponse = birdsContext.toTransport() as TweetSearchResponse
    respond(tweetSearchResponse)
}

suspend fun ApplicationCall.birdsDelete(settings: AppSettings) {
    val req = receive<TweetDeleteRequest>()
    val processor = settings.corSettings
    val birdsContext = BirdsContext()
    birdsContext.fromTransport(req)
    processor.process(birdsContext)
    val tweetDeleteResponse = birdsContext.toTransport() as TweetDeleteResponse
    respond(tweetDeleteResponse)
}