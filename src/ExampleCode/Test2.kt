package ExampleCode

/**
 * Test2
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2024. 11. 11.
 */
fun main() {
    val list = listOf(
        Auth("b", false),
        Auth("c", false),
        Auth("d", false),
        Auth("a", true),
    )

    val result = list.any {it.b }
    println("result: $result")
}

data class Auth(
    val a: String,
    val b: Boolean
)