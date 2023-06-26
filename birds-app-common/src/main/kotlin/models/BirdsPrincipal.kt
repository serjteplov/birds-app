package models

import BirdsTweetPermission

data class BirdsPrincipal(
    val id: String,
    val groups: List<BirdsGroup>,
    val allowedPermissions: List<BirdsTweetPermission>,
    val deniedPermissions: List<BirdsTweetPermission>,
    val permissions: List<BirdsTweetPermission>,
) {
    companion object {}
}
