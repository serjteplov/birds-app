package ru.serj.birds.settings

import settings.BirdsLoggerSettings
import ru.serj.biz.processor.BirdsMainProcessor

data class AppSettings (
    val corSettings: BirdsMainProcessor,
    val logSettings: BirdsLoggerSettings
)