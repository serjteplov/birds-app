package ru.serj.domain.entity

data class DbError(
    val cause: String,
    val message: String? = null
)