package ru.serj.birds.reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
@ConfigurationPropertiesScan
class BirdsAppSpringWeb

fun main() {
    runApplication<BirdsAppSpringWeb>()
}
