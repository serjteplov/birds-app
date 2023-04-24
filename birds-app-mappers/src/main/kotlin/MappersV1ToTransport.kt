import models.*
import ru.serj.api.v1.models.*

fun BirdsContext.toTransport(): TResponse = when (command) {
    BirdsCommand.CREATE -> toTransportCreate()
    BirdsCommand.DELETE -> toTransportDelete()
    BirdsCommand.SEARCH -> toTransportSearch()
    BirdsCommand.FILTER -> toTransportFilter()
    BirdsCommand.NONE -> throw UnknownCommandException(command)
    BirdsCommand.READ -> throw UnsupportedCommandException(command)
    BirdsCommand.UPDATE -> throw UnsupportedCommandException(command)
}

private fun BirdsContext.toTransportFilter() = TweetFilterResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == BirdsState.DONE) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransport(),
    tweets = tweetMultiResponse.map { it.toTransport() }.takeIf { it.isNotEmpty() }
)

private fun BirdsContext.toTransportDelete() = TweetDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == BirdsState.DONE) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransport(),
    id = tweetResponse.id.asString(),
    ownerId = tweetResponse.ownerId.asString(),
)

private fun BirdsContext.toTransportSearch() = TweetSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == BirdsState.DONE) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransport(),
    tweets = tweetMultiResponse.map { it.toTransport() }.takeIf { it.isNotEmpty() }
)

private fun BirdsContext.toTransportCreate() = TweetCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == BirdsState.DONE) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransport(),
    text = tweetResponse.toTransportText(),
    containsMedia = tweetResponse.containsMedia,
    reply = tweetResponse.type.toTransport(),
    visibility = tweetResponse.visibility.toTransport(),
    id = tweetResponse.id.asString(),
    ownerId = tweetResponse.ownerId.asString(),
    permissions = tweetResponse.permissions.map { it.toTransport() }.toSet().takeIf { it.isNotEmpty() },
    version = tweetResponse.version
)

private fun BirdsTweet.toTransport() = TweetSingleResponse(
    text = text,
    containsMedia = containsMedia,
    reply = type.toTransport(),
    visibility = visibility.toTransport(),
    id = id.asString(),
    ownerId = ownerId.asString(),
    permissions = permissions.map { it.toTransport() }.takeIf { it.isNotEmpty() }?.toSet(),
    version = version
)

fun BirdsTweetPermission.toTransport() = when (this) {
    BirdsTweetPermission.READ -> TweetPermissions.READ
    BirdsTweetPermission.UPDATE -> TweetPermissions.UPDATE
    BirdsTweetPermission.DELETE -> TweetPermissions.DELETE
}

fun BirdsTweetVisibility.toTransport() = when (this) {
    BirdsTweetVisibility.NONE -> TweetVisibility.PUBLIC
    BirdsTweetVisibility.VISIBLE_TO_OWNER -> TweetVisibility.FOLLOWERS_ONLY
    BirdsTweetVisibility.VISIBLE_TO_GROUP -> TweetVisibility.FOLLOWERS_ONLY
    BirdsTweetVisibility.VISIBLE_TO_PUBLIC -> TweetVisibility.PUBLIC
}

fun BirdsTweetType.toTransport() = when (this) {
    BirdsTweetType.NONE -> false
    BirdsTweetType.ORIGINAL -> false
    BirdsTweetType.REPLY -> true
}

fun List<BirdsError>.toTransport() = map{ it.toTransport() }.toList().takeIf { it.isNotEmpty() }

fun BirdsTweet.toTransportText() = this.text

fun BirdsError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)