package ru.serj.birds.mapping

import mapper
import ru.serj.api.v1.models.TRequest

@Suppress("UNCHECKED_CAST")
fun <T : TRequest>rawToRequest(input: String): T {
    return mapper.readValue(input, TRequest::class.java) as T
}
