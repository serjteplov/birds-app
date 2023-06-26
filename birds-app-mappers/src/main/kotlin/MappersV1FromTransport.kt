import kotlinx.datetime.*
import models.*
import ru.serj.api.v1.models.*
import java.util.UUID

fun BirdsContext.fromTransport(request: TRequest) = when (request) {
    is TweetCreateRequest -> fromTransport(request)
    is TweetDeleteRequest -> fromTransport(request)
    is TweetFilterRequest -> fromTransport(request)
    is TweetSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}

fun String?.toTweetId() = this?.let { BirdsTweetId(it) } ?: BirdsTweetId.NONE
fun TRequest?.requestId() = this?.requestId?.let { BirdsRequestId(it) } ?: BirdsRequestId.NONE
fun TweetVisibility?.fromTransport() = when (this) {
    TweetVisibility.TO_USER -> BirdsTweetVisibility.TO_USER
    TweetVisibility.TO_OWNER -> BirdsTweetVisibility.TO_OWNER
    TweetVisibility.TO_GUEST -> BirdsTweetVisibility.TO_GUEST
    TweetVisibility.TO_FOLLOWER -> BirdsTweetVisibility.TO_FOLLOWER
    else -> BirdsTweetVisibility.NONE
}

fun Boolean?.fromTransport() = when (this) {
    true -> BirdsTweetType.REPLY
    false -> BirdsTweetType.ORIGINAL
    else -> BirdsTweetType.NONE
}

fun String?.toTweetWithId() = BirdsTweet(
    id = this.toTweetId()
)

fun TweetDebug?.transportToWorkMode() = when (this?.mode) {
    TweetRequestDebugMode.PROD -> BirdsWorkMode.PROD
    TweetRequestDebugMode.TEST -> BirdsWorkMode.TEST
    TweetRequestDebugMode.STUB -> BirdsWorkMode.STUB
    null -> BirdsWorkMode.PROD
}

fun TweetDebug?.transportToStubCase() = when (this?.stub) {
    TweetRequestDebugStubs.SUCCESS -> BirdsStubs.SUCCESS
    TweetRequestDebugStubs.NOT_FOUND -> BirdsStubs.NOT_FOUND
    TweetRequestDebugStubs.BAD_ID -> BirdsStubs.BAD_ID
    TweetRequestDebugStubs.BAD_TITLE -> BirdsStubs.BAD_TITLE
    TweetRequestDebugStubs.BAD_DESCRIPTION -> BirdsStubs.BAD_DESCRIPTION
    TweetRequestDebugStubs.BAD_VISIBILITY -> BirdsStubs.BAD_VISIBILITY
    TweetRequestDebugStubs.CANNOT_DELETE -> BirdsStubs.CANNOT_DELETE
    TweetRequestDebugStubs.BAD_SEARCH_STRING -> BirdsStubs.BAD_SEARCH_STRING
    null -> BirdsStubs.NONE
}

fun TweetCreateObject?.toInternal(principal: BirdsPrincipal) = BirdsTweet(
    id = BirdsTweetId(UUID.randomUUID().toString()),
    text = this?.text ?: "",
    containsMedia = this?.containsMedia ?: false,
    type = this?.reply.fromTransport(),
    visibility = this?.visibility.fromTransport(),
    createdAt = Clock.System.now(),
    ownerId = BirdsUserId(principal.id)
)

fun TweetSearchText?.toInternal() = BirdsTweetSearch(
    searchString = this?.searchString ?: ""
)

fun BirdsContext.fromTransport(request: TweetCreateRequest) {
    command = BirdsCommand.CREATE
    requestId = request.requestId()
    tweetRequest = request.tweet?.toInternal(principal) ?: BirdsTweet()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun BirdsContext.fromTransport(request: TweetDeleteRequest) {
    command = BirdsCommand.DELETE
    requestId = request.requestId()
    tweetRequest = request.tweetDelete?.id.toTweetWithId().copy(ownerId = BirdsUserId(principal.id))
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun BirdsContext.fromTransport(request: TweetFilterRequest) {
    command = BirdsCommand.FILTER
    requestId = request.requestId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
    val parsingResult = runCatching { request.interval?.toInternal() ?: BirdsFilterPeriod() }
    tweetFilterPeriod = parsingResult.takeIf { it.isSuccess }?.getOrNull() ?: BirdsFilterPeriod()
    errors = parsingResult.takeIf { it.isFailure }?.let {
        mutableListOf(
            BirdsError(
                code = "1000",
                group = "parsing",
                field = "tweetFilterPeriod",
                message = it.exceptionOrNull()?.message ?: "",
                exception = it.exceptionOrNull()
            )
        )
    } ?: mutableListOf()
}

fun TweetFilterInterval.toInternal() = BirdsFilterPeriod(
    from = from?.let { LocalDateTime.parse(it).toInstant(TimeZone.UTC) } ?: Instant.NONE,
    to = to?.let { LocalDateTime.parse(it).toInstant(TimeZone.UTC) } ?: Instant.NONE
)

fun BirdsContext.fromTransport(request: TweetSearchRequest) {
    command = BirdsCommand.SEARCH
    requestId = request.requestId()
    tweetSearchRequest = request.tweetFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}