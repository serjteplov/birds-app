package ru.serj.biz.authorize

import BirdsContext
import BirdsPrincipalRelation
import BirdsPrincipalRelation.FOLLOWER
import BirdsPrincipalRelation.NONE
import BirdsPrincipalRelation.OWNER
import BirdsTweetPermission
import BirdsTweetPermission.*
import BirdsTweetVisibility.*
import models.*
import models.BirdsCommand.*
import ru.serj.dsl.BaseChainDsl

fun BaseChainDsl<BirdsContext>.authorizationVerdict(name: String, block: BaseChainDsl<BirdsContext>.() -> Unit) =
    chain {
        title = name
        description = "Авторизация"
        on {
            state == BirdsState.RUNNING
                    && !authorized
                    && (workMode == BirdsWorkMode.TEST || workMode == BirdsWorkMode.PROD)
        }
        block()
    }

fun BaseChainDsl<BirdsContext>.calculateRelationEdit(name: String) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING && stubCase == BirdsStubs.NONE
        }
        handle {
            // вычислить principal relations и положить в контекст
            val relations = mutableSetOf<BirdsPrincipalRelation>()

            if (tweetRequest.ownerId.asString() == principal.id)
                relations.add(OWNER)

            if (tweetRequest.ownerId.asString() != principal.id
                && tweetRequest.ownerId.asString() > principal.id
            ) relations.add(FOLLOWER)

            if (tweetRequest.ownerId.asString() != principal.id
            ) relations.add(NONE)

            principalRelations = relations.toSet()
        }
    }

fun BaseChainDsl<BirdsContext>.calculateRelationRead(name: String) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING && stubCase == BirdsStubs.NONE
        }
        handle {
            // для простоты полагаем что искать по твитам можно только в своей ленте
            principalRelations = setOf(FOLLOWER)
        }
    }

fun BaseChainDsl<BirdsContext>.permissionsForFront(name: String) =
    worker {
        title = name
        on {
            state == BirdsState.DONE
                    && stubCase == BirdsStubs.NONE
                    && (workMode == BirdsWorkMode.TEST || workMode == BirdsWorkMode.PROD)
                    && authorized
        }
        handle {
            val responsePermissions = resolveFrontPermissions().toMutableList()
            if (tweetResponse.id != BirdsTweetId.NONE) {
                tweetResponse.permissions = responsePermissions
            }
            if (tweetMultiResponse.isNotEmpty()) {
                tweetMultiResponse.forEach {
                    it.permissions = responsePermissions
                }
            }
        }
    }

fun BaseChainDsl<BirdsContext>.makeVerdict(name: String) =
    worker {
        title = name
        on {
            state == BirdsState.RUNNING && stubCase == BirdsStubs.NONE
        }
        handle {
            val relation = principalRelations.single()
            val filteredPermissions = principal.permissions
                .map { it.toString() }
                .filter {
                    val com = if (command in listOf(FILTER, SEARCH)) "READ" else command.toString()
                    it.contains(com)
                }
            val check1 = filteredPermissions
                .any { it.contains("MODERATOR") }
            if (check1) {
                authorized = true
                return@handle
            }

            // EDIT
            if (command in listOf(CREATE, DELETE) && filteredPermissions.isEmpty()) {
                authorized = false
                errors = mutableListOf(
                    BirdsError(
                        code = "SECURITY_PROBLEM",
                        group = "Authorization",
                        message = "User ${principal.id} not authorized for this action"
                    )
                )
                return@handle
            }
            if (command in listOf(CREATE, DELETE) && filteredPermissions.isNotEmpty() && relation == OWNER) {
                authorized = true
                return@handle
            }
            if (command in listOf(CREATE, DELETE) && filteredPermissions.isNotEmpty() && relation != OWNER) {
                authorized = false
                errors = mutableListOf(
                    BirdsError(
                        code = "SECURITY_PROBLEM",
                        group = "Authorization",
                        message = "User ${principal.id} not authorized for this action"
                    )
                )
                return@handle
            }

            // READ
            if (command in listOf(FILTER, SEARCH) && filteredPermissions.isEmpty()) {
                authorized = false
                errors = mutableListOf(
                    BirdsError(
                        code = "SECURITY_PROBLEM",
                        group = "Authorization",
                        message = "User ${principal.id} not authorized for this action"
                    )
                )
                return@handle
            }
            if (command in listOf(FILTER, SEARCH) && filteredPermissions.isNotEmpty() && relation == OWNER) {
                authorized = true
                visibilitiesAllowed = setOf(TO_OWNER, TO_FOLLOWER, TO_USER, TO_GUEST)
                return@handle
            }
            if (command in listOf(FILTER, SEARCH) && filteredPermissions.isNotEmpty() && relation == FOLLOWER) {
                authorized = true
                visibilitiesAllowed = setOf(TO_FOLLOWER, TO_USER, TO_GUEST)
                return@handle
            }
            if (command in listOf(FILTER, SEARCH) && filteredPermissions.isNotEmpty() && relation == NONE) {
                authorized = true
                visibilitiesAllowed = setOf(TO_USER, TO_GUEST)
                return@handle
            }
        }
    }


fun BirdsContext.resolveFrontPermissions(): Set<BirdsTweetPermission> {
    val usersPermissions = principal.permissions.filter { it.toString().contains("USERS") }.toSet()
    val usersPermissionsRead = principal.permissions.filter { it.toString().contains("READ_USERS") }.toSet()

    if (principalRelations.single() == OWNER) {
        return usersPermissions
    }
    if (principalRelations.single() == FOLLOWER) {
        return usersPermissionsRead
    }

    // NONE
    val modersPermissions = principal.permissions.filter { it.toString().contains("MODERATOR") }.toSet()
    val guestPermissions = principal.permissions.filter { it.toString().contains("GUEST") }.toSet()
    if (modersPermissions.isNotEmpty()) {
        return modersPermissions
    }
    if (usersPermissionsRead.isNotEmpty()) {
        return usersPermissionsRead
    }
    if (guestPermissions.isNotEmpty()) {
        return guestPermissions
    }
    return emptySet()
}

val table: Map<BirdsPrincipalRelation, List<BirdsTweetPermission>> = mapOf(
    OWNER to listOf(CREATE_USERS, READ_USERS, UPDATE_USERS, DELETE_USERS),
    FOLLOWER to listOf(READ_USERS),
    NONE to listOf(READ_GUESTS, READ_USERS, READ_MODERATORS, UPDATE_MODERATORS, DELETE_MODERATORS)
)
