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
    tweets = tweetMultiResponse.map { it.toTransport() }
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
    tweets = tweetMultiResponse.map { it.toTransport() }
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
    BirdsTweetPermission.CREATE_USERS -> TweetPermissions.CREATE_OWN
    BirdsTweetPermission.READ_USERS -> TweetPermissions.READ_OWN
    BirdsTweetPermission.UPDATE_USERS -> TweetPermissions.UPDATE_OWN
    BirdsTweetPermission.DELETE_USERS -> TweetPermissions.DELETE_OWN
    BirdsTweetPermission.UPDATE_MODERATORS -> TweetPermissions.UPDATE_MODERATE
    BirdsTweetPermission.DELETE_MODERATORS -> TweetPermissions.DELETE_MODERATE
    BirdsTweetPermission.READ_MODERATORS -> TweetPermissions.READ_MODERATE
    BirdsTweetPermission.READ_GUESTS -> TweetPermissions.READ_PUBLIC
}

fun BirdsTweetVisibility.toTransport() = when (this) {
    BirdsTweetVisibility.NONE -> null
    BirdsTweetVisibility.TO_OWNER -> TweetVisibility.TO_OWNER
    BirdsTweetVisibility.TO_FOLLOWER -> TweetVisibility.TO_FOLLOWER
    BirdsTweetVisibility.TO_USER -> TweetVisibility.TO_USER
    BirdsTweetVisibility.TO_GUEST -> TweetVisibility.TO_GUEST
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