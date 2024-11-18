package ExampleCode

/**
 * Test3
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2024. 11. 18.
 */
fun main() {
    val list: List<String>? = null
    val map = list?.map { Gyun("A", "B") }
    println("map: $map")
}

data class Gyun(
    val a: String,
    val b: String
)