package ru.serj.birds

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import mapper
import org.slf4j.event.Level
import ru.serj.birds.route.birdsCreate
import ru.serj.birds.route.birdsDelete
import ru.serj.birds.route.birdsFilter
import ru.serj.birds.route.birdsSearch
import ru.serj.birds.settings.AppSettings
import ru.serj.birds.settings.initAppSettings

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(settings: AppSettings = initAppSettings()) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(Locations)
    install(CallLogging) {
        level = Level.DEBUG
    }

    install(ContentNegotiation) {
        jackson {
            setConfig(mapper.serializationConfig)
            setConfig(mapper.deserializationConfig)
        }
    }

    routing {
        route("/bird/v1") {
            post("create") {
                call.application.environment.log.info("Ktor log says Hello!")
                call.birdsCreate(settings)
            }
            post("filter") {
                call.birdsFilter()
            }
            post("delete") {
                call.birdsDelete()
            }
            post("search") {
                call.birdsSearch()
            }
        }
    }
}
