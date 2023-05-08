package models

import NONE
import kotlinx.datetime.Instant

data class BirdsFilterPeriod(
    var from: Instant = Instant.NONE,
    var to: Instant = Instant.NONE
)
