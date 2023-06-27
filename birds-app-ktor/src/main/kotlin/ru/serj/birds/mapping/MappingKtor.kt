package ru.serj.birds.mapping

import BirdsTweetPermission
import groupPermissions
import io.ktor.server.auth.jwt.*
import models.BirdsGroup
import models.BirdsPrincipal

fun JWTPrincipal.toBirdsPrincipal(): BirdsPrincipal {
    val groups = this.payload.getClaim("groups")
        ?.asArray(BirdsGroup::class.java)
        ?.toList()
        ?: listOf(BirdsGroup.NONE)
    val deniedPermissions = this.payload.getClaim("deny")
        ?.asString()
        ?.uppercase()
        ?.split(",")
        ?.map { it.trim() }
        ?.map { BirdsTweetPermission.valueOf(it) }
        ?: emptyList()
    val allowedPermissions = groupsToPermissions(groups)
    val principalId = this.payload.getClaim("sub").asString()

    return BirdsPrincipal(
        id = principalId,
        allowedPermissions = allowedPermissions,
        deniedPermissions = deniedPermissions,
        permissions = allowedPermissions.minus(deniedPermissions.toSet()),
        groups = groups
    )
}

fun groupsToPermissions(groups: List<BirdsGroup>): List<BirdsTweetPermission> {
    return groups.flatMap { groupPermissions[it] ?: emptyList() }
}