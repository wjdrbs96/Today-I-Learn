## `coroutine 시작하기`

## `루틴 (동기적으로 실행)`

- 새로운 루틴(함수)이 호출되면 사용되는 스택에 지역 변수 초기화
- 루틴(함수)이 끝나면 해당 메모리에 접근 불가능
- 루틴에 진입하는 곳이 한 군데이며, 종료되면 해당 루틴의 정보가 초기화된다.

```kotlin
  fun main() {
  println("START")
  newRoutine()
  println("END")
}

fun newRoutine() {
  val num1 = 1
  val num2 = 2
  println("${num1 + num2}")
}
```

<br>

### `코루틴`

```kotlin
fun main() = runBlocking {
    println("START")
    launch {
        newRoutine()
    }
    yield()
    println("END")
}

suspend fun newRoutine() {
    val num1 = 1
    val num2 = 2
    yield()
    println("${num1 + num2}")
}
```

- runBlocking: 부모 코루틴 생성
- launch: 자식 코루틴 생성
- yield(): 지금 코루틴을 중단하고 다른 코루틴이 실행되도록 함
- 새로운 루틴이 호출된 후 완전히 종료되기 전, 해당 루틴에서 사용했던 정보들을 보관하고 있어야 함 (중단, 재개라는 개념이 존재하기 때문)

<br>

## `스레드와 코루틴`

