import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import route.birdsCreate
import route.birdsDelete
import route.birdsFilter
import route.birdsSearch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(Locations)
    install(CallLogging) {
        level = Level.DEBUG
    }

    install(ContentNegotiation) {
        jackson {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

    routing {
        route("/bird/v1") {
            post("create") {
                call.application.environment.log.info("Hello !")
                call.birdsCreate()
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
