package models

@JvmInline
value class BirdsUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BirdsUserId("")
    }
}