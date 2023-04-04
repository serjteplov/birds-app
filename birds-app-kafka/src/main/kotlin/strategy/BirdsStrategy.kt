package strategy

import BirdsContext
import ru.serj.api.v1.models.TRequest

sealed interface BirdsStrategy {
    val requestType: String

    fun process(request: TRequest, context: BirdsContext)

    companion object {
        val strategies = listOf(
            CreateStrategy(),
            DeleteStrategy(),
            FilterStrategy(),
            SearchStrategy()
        ).associateBy { it.requestType }
    }
}
