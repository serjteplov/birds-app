package models

@JvmInline
value class BirdsRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BirdsRequestId("")
    }
}