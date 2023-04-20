package ru.serj.handlers

interface BaseHandler<T> {

    val title: String

    val description: String

    suspend fun exec(context: T)
}