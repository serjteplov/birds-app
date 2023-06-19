package playground

val userContext: UserContext = UserContext()

class A{
    fun fuu() {
        user {
            testString = "42"
        }
        userContext.block2()
    }
}

class UserContext {
    var testString: String = ""
}

fun UserContext.block2() {
    testString = "42"
}

fun user(block: UserContext.() -> Unit) {

    userContext.block()
    block.invoke(userContext)
    block(userContext)

    UserContext().block()
    block.invoke(UserContext())
    block(UserContext())
}

fun main() {
    A().fuu()
}
