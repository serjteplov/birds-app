
enum class BirdsTweetType {
    NONE,
    ORIGINAL,
    REPLY
}

enum class BirdsTweetVisibility {
    NONE,
    TO_OWNER,
    TO_FOLLOWER,
    TO_USER,
    TO_GUEST
}
enum class BirdsTweetPermission {
    READ_GUESTS,

    CREATE_USERS,
    READ_USERS,
    UPDATE_USERS,
    DELETE_USERS,

    READ_MODERATORS,
    UPDATE_MODERATORS,
    DELETE_MODERATORS
}

enum class BirdsPrincipalRelation {
    OWNER,
    FOLLOWER,
    NONE,
}