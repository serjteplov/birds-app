package models

@JvmInline
value class BirdsTweetId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BirdsTweetId("")
    }
}