package ru.serj.birds.reactive.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.serj.birds.reactive.service.MyService

@Configuration
class MyConfiguration {

    @Bean
    fun myService(): MyService {
        return MyService()
    }

}