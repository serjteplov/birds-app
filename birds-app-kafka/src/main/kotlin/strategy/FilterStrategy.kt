package strategy

import BirdsContext
import fromTransport
import models.BirdsState
import ru.serj.api.v1.models.TRequest
import ru.serj.birds.stub.tweetResponseStub

class FilterStrategy : BirdsStrategy {

    override val requestType: String = "filter"

    override fun process(request: TRequest, context: BirdsContext) {
        context.fromTransport(request)
        context.tweetResponse = tweetResponseStub
        context.state = BirdsState.RUNNING
    }
}
