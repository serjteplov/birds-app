package ru.serj.birds.reactive.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.flux
import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.serj.api.v1.models.Error
import ru.serj.api.v1.models.ResponseResult
import ru.serj.api.v1.models.TweetFilterRequest
import ru.serj.api.v1.models.TweetFilterResponse
import ru.serj.birds.reactive.service.MyService

@RestController
class MyController(
    private val myService: MyService
) {

    @PostMapping("/bird/v2/flux")
    suspend fun getFiltered(req: TweetFilterRequest): Flux<TweetFilterResponse> {
        val resp1 = TweetFilterResponse(
            requestId = "requestId1",
            result = ResponseResult.SUCCESS
        )
        val resp2 = TweetFilterResponse(
            requestId = "requestId2",
            result = ResponseResult.SUCCESS
        )
        val resp3 = TweetFilterResponse(
            requestId = "requestId3",
            result = ResponseResult.SUCCESS
        )
        val flux = Flux.just(resp1, resp2, resp3)
        return flux
    }
    @PostMapping("/bird/v2/mono")
    suspend fun getFilteredMono(req: TweetFilterRequest): Mono<TweetFilterResponse> {
        val resp1 = TweetFilterResponse(
            requestId = "requestId1",
            result = ResponseResult.SUCCESS
        )
        return mono {
            delay(3000)
            resp1
        }
    }
}