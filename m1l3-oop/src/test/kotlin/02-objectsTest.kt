import org.junit.Test

class ObjectsExample {
    object Singleton{
        init {
            println("Singleton class invoked.")
        }
        const val variableName = "I am Var"
        fun printVarName(){
            println(variableName)
        }

    }
    companion object {
        init {
            println("companion inited") // инициализация при загрузке класса ObjectsExample
        }
        fun doSmth() {
            println("companion object")
        }
    }

    object A {
        init {
            println("A inited") // ленивая инициализация при первом обращении к А
        }
        fun doSmth() {
            println("object A")
        }
    }
}

class ObjectsTest {
    @Test
    fun test() {
        ObjectsExample()
        ObjectsExample.doSmth()
        ObjectsExample.A.doSmth()
        ObjectsExample.A.doSmth()
        ObjectsExample.Singleton.printVarName()
    }
}

