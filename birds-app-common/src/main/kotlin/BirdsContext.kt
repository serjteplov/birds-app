import kotlinx.datetime.Instant
import models.*

data class BirdsContext(
    var command: BirdsCommand = BirdsCommand.NONE,
    var state: BirdsState = BirdsState.NONE,
    var errors: MutableList<BirdsError> = mutableListOf(),

    var workMode: BirdsWorkMode = BirdsWorkMode.PROD,
    var stubCase: BirdsStubs = BirdsStubs.NONE,

    var requestId: BirdsRequestId = BirdsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var tweetRequest: BirdsTweet = BirdsTweet(),
    var tweetFilterRequest: BirdsTweetSearch = BirdsTweetSearch(),
    var tweetFilterPeriod: BirdsFilterPeriod = BirdsFilterPeriod(),
    var tweetResponse: BirdsTweet = BirdsTweet(),
    var tweetMultiResponse: MutableList<BirdsTweet> = mutableListOf()
)