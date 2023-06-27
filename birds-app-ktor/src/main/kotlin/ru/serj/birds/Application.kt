package ru.serj.birds

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mapper
import org.slf4j.event.Level
import ru.serj.birds.route.birdsCreate
import ru.serj.birds.route.birdsDelete
import ru.serj.birds.route.birdsFilter
import ru.serj.birds.route.birdsSearch
import ru.serj.birds.settings.AppSettings
import ru.serj.birds.settings.AuthSettings
import ru.serj.birds.settings.getAuth
import ru.serj.birds.settings.initAppSettings
import java.net.URL
import java.security.interfaces.RSAPublicKey

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.moduleBird(settings: AppSettings = initAppSettings(), authSettings: AuthSettings = getAuth()) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(Locations)
    install(CallLogging) {
        level = Level.TRACE
    }
    install(ContentNegotiation) {
        jackson {
            setConfig(mapper.serializationConfig)
            setConfig(mapper.deserializationConfig)
        }
    }
    install(Authentication) {
        jwt {
            realm = "otus-marketplace"
            verifier {
                val algorithm = it.getPublicKey(authSettings)
                JWT
                    .require(algorithm)
                    .withAudience(authSettings.audience)
                    .withIssuer(authSettings.issuer)
                    .build()
            }
            validate { jwt ->
                if (jwt.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(jwt.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        authenticate {
            route("/bird/v1") {
                post("create") {
                    call.application.environment.log.info("Ktor log says Hello!")
                    call.birdsCreate(settings)
                }
                post("filter") {
                    call.birdsFilter(settings)
                }
                post("delete") {
                    call.birdsDelete(settings)
                }
                post("search") {
                    call.birdsSearch(settings)
                }
            }
        }
    }
}

private fun HttpAuthHeader.getPublicKey(authSettings: AuthSettings): Algorithm {
    if (authSettings.issuer.isNullOrBlank()) {
        return Algorithm.HMAC256(authSettings.secret)
    }
    val tokenString = render().replace(authScheme, "").trim()
    val token = JWT.decode(tokenString)
    val keyId = token.keyId
    val provider = UrlJwkProvider(URL(authSettings.certPath))
    val jwk = provider.get(keyId)
    val publicKey = jwk.publicKey as RSAPublicKey
    return Algorithm.RSA256(publicKey, null)
}
