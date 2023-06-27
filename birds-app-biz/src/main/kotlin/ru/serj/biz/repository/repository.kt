package ru.serj.biz.repository

import BirdsContext
import models.BirdsState
import models.BirdsStubs
import models.BirdsTweet
import models.BirdsWorkMode
import ru.serj.domain.entity.DbRequest
import ru.serj.domain.repository.BirdsTweetRepository
import ru.serj.dsl.BaseChainDsl
import toBirdsError

fun BaseChainDsl<BirdsContext>.repository(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) =
    chain {
        title = name
        description = "Работа с базой данных"
        on { state == BirdsState.RUNNING }
        block()
    }

fun BaseChainDsl<BirdsContext>.saveTweetTemporary(name: String, repo: BirdsTweetRepository) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING
                    && stubCase == BirdsStubs.NONE
                    && (workMode == BirdsWorkMode.TEST || workMode == BirdsWorkMode.PROD)
                    && authorized
        }
        handle {
            val res = repo.createBirdsTweet(DbRequest(tweet = tweetRequest))
            if (res.errors.isEmpty()) {
                state = BirdsState.DONE
                tweetResponse = tweetRequest.copy(permissions = mutableListOf(), version = "1")
            }
            else {
                state = BirdsState.FAILED
                errors = res.errors.map { it.toBirdsError() }.toMutableList()
            }
        }
    }

fun BaseChainDsl<BirdsContext>.filterTweetByDate(name: String, repo: BirdsTweetRepository) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING
                    && stubCase == BirdsStubs.NONE
                    && workMode == BirdsWorkMode.TEST
        }
        handle {
            val res = repo.filterBirdsTweet(
                DbRequest(
                    from = tweetFilterPeriod.from,
                    to = tweetFilterPeriod.to,
                    visibilities = visibilitiesAllowed
                )
            )
            if (res.errors.isEmpty()) {
                state = BirdsState.DONE
                tweetMultiResponse = res.tweets.toMutableList()
            }
            else {
                state = BirdsState.FAILED
                errors = res.errors.map { it.toBirdsError() }.toMutableList()
            }
        }
    }

fun BaseChainDsl<BirdsContext>.requestDeletingObject(name: String, repo: BirdsTweetRepository) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING
                    && stubCase == BirdsStubs.NONE
                    && (workMode == BirdsWorkMode.TEST || workMode == BirdsWorkMode.PROD)
        }
        handle {
            val res = repo.findById(DbRequest(id = tweetRequest.id))
            if (res.errors.isEmpty()) {
                tweetRequest = res.tweet ?: BirdsTweet()
            }
            else {
                state = BirdsState.FAILED
                errors = res.errors.map { it.toBirdsError() }.toMutableList()
            }
        }
    }

fun BaseChainDsl<BirdsContext>.deleteTweetFromTemporary(name: String, repo: BirdsTweetRepository) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING
                    && stubCase == BirdsStubs.NONE
                    && (workMode == BirdsWorkMode.TEST || workMode == BirdsWorkMode.PROD)
                    && authorized
        }
        handle {
            val res = repo.deleteBirdsTweet(DbRequest(id = tweetRequest.id))
            if (res.errors.isEmpty()) {
                state = BirdsState.DONE
                tweetResponse = tweetRequest
            }
            else {
                state = BirdsState.FAILED
                errors = res.errors.map { it.toBirdsError() }.toMutableList()
            }
        }
    }

fun BaseChainDsl<BirdsContext>.simpleTweetSearch(name: String, repo: BirdsTweetRepository) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING
                    && stubCase == BirdsStubs.NONE
                    && workMode == BirdsWorkMode.TEST
        }
        handle {
            val res = repo.searchBirdsTweet(
                DbRequest(
                    search = tweetSearchRequest.searchString,
                    visibilities = visibilitiesAllowed
                )
            )
            if (res.errors.isEmpty()) {
                state = BirdsState.DONE
                tweetMultiResponse = res.tweets.toMutableList()
            }
            else {
                state = BirdsState.FAILED
                errors = res.errors.map { it.toBirdsError() }.toMutableList()
            }
        }
    }

