package ru.serj.postgres.entity

import BirdsTweetVisibility
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*


object Tweets : UUIDTable("tweets2") {
    val text = varchar("text", 200)
    val containsMedia = bool("contains_media")
    val reply = bool("reply")
    val ownerId = varchar("owner_id", 50)
    val visibility = enumerationByName("visibility", 30, BirdsTweetVisibility::class)
    val version = varchar("version", 3)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}

class BirdsTweetEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BirdsTweetEntity>(Tweets)
    var containsMedia by Tweets.containsMedia
    var reply by Tweets.reply
    var visibility by Tweets.visibility
    var text by Tweets.text
    var ownerId by Tweets.ownerId
    var version by Tweets.version
    var createdAt by Tweets.createdAt
}