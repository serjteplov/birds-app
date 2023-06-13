package ru.serj.domain.repository

import ru.serj.domain.entity.DbRequest
import ru.serj.domain.entity.DbResponse

interface BirdsTweetRepository {

    fun createBirdsTweet(request: DbRequest): DbResponse

    fun deleteBirdsTweet(request: DbRequest): DbResponse

    fun filterBirdsTweet(request: DbRequest): DbResponse

    fun searchBirdsTweet(request: DbRequest): DbResponse
}