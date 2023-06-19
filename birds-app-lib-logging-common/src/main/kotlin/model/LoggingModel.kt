package model

import java.time.Instant

data class LoggingModel (
    val dateTime: Instant,
    val message: String,
    val model: Any?,
    val error: String?
)