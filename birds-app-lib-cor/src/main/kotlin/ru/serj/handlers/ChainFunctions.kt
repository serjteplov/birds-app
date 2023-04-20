package ru.serj.handlers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun <T> executeSequential(context: T, workers: List<BaseWorker<T>>) {
    workers.forEach { it.exec(context) }
}

suspend fun <T> executeParallel(context: T, workers: List<BaseWorker<T>>) = coroutineScope {
    workers.forEach { launch { it.exec(context) } }
}