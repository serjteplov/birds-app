import models.BirdsError
import ru.serj.domain.entity.DbError

fun DbError.toBirdsError() = BirdsError(
    code = "DB_ERROR",
    group = "Database",
    field = cause,
    message = message ?: "",
)