import kotlinx.datetime.Instant
import models.BirdsGroup
import models.BirdsPrincipal

val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)

val Instant.Companion.NONE
    get() = INSTANT_NONE

val BirdsPrincipal.Companion.NONE
    get() = BirdsPrincipal("", emptyList(), emptyList(), emptyList(), emptyList())

val groupPermissions: Map<BirdsGroup,List<BirdsTweetPermission>> = mapOf(
    BirdsGroup.USERS to listOf(
        BirdsTweetPermission.READ_GUESTS,
        BirdsTweetPermission.CREATE_USERS,
        BirdsTweetPermission.READ_USERS,
        BirdsTweetPermission.UPDATE_USERS,
        BirdsTweetPermission.DELETE_USERS,
    ),
    BirdsGroup.MODERATORS to listOf(
        BirdsTweetPermission.UPDATE_MODERATORS,
        BirdsTweetPermission.DELETE_MODERATORS,
    )
)