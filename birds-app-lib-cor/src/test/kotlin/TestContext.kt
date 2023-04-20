data class TestContext (
    var name: String,
    var number: Int,
    var phase: String,
    var status: CorStatus = CorStatus.NONE
)

enum class CorStatus {
    NONE,
    RUNNING,
    FAILING,
    ERROR
}
