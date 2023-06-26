package ru.serj.birds.settings

data class AuthSettings (
    val issuer: String? = null,
    val audience: String,
    val realm: String,
    val secret: String,
    val certPath: String
) {
    companion object {
        val TEST = AuthSettings(
            audience = "testAudience",
            realm = "testRealm",
            secret = "testSecret",
            certPath = "testCertPath"
        )
    }
}
