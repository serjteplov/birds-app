package homework

data class Dictionary (
    val word: String,
    val synonyms: Array<String>? = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dictionary) return false

        if (word != other.word) return false
        if (!synonyms.contentEquals(other.synonyms)) return false

        return true
    }
    override fun hashCode(): Int {
        var result = word.hashCode()
        result = 31 * result + synonyms.contentHashCode()
        return result
    }
}