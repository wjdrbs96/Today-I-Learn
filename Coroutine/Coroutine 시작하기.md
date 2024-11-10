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

- 스레드
  - 프로세스보다 작은 개념 
  - context switching 발생시 Heap 메모리를 공유하고, Stack만 교체되므로 프로세스 보다 비용이 적다.
  - OS가 스레드를 강제로 멈추고 다른 스레드를 실행한다.
- 코루틴
  - 스레드보다 작은 개념이다.
  - 한 코루틴 코드는 여러 스레드에서 실행될 수 있다.
  - 한 스레드에서 실행하는 경우 context switching 발생시 메모리 교체가 없다.
  - 코루틴 스스로가 다른 코루틴에게 양보한다.

<br>

## `코루틴 빌더와 Job`

- `runBlocking`
  - 코루틴과 루틴을 연결해주는 코루틴 빌더
- `lanuch`: 반환 값 없음
- `async`: 반환값 존재 (Deffered)
- 등등

<br>

## `부모 코루틴, 자식 코루틴`

```kotlin
fun main(): Unit = runBlocking {
    launch {
        delay(500L)
        println("A")
    }

    launch {
        delay(600L)
        throw IllegalArgumentException("Coroutine Fail")
    }
}
```

- Structured Concurrency 부모와 자식이 하나처럼 동작한다.
- 자식에서 예외 발생하면 부모로 전파
- 부모에서 예외가 발생했기 때문에 다른 자식들도 예외 전파
- 다만, CancellationException은 정상적인 취소로 간주하기 때문에 부모 코루틴에게 전파되지 않고, 부모 코루틴의 다른 자식 코루틴을 취소시키지도 않는다.

<br>

## `CoroutineDispatchers`

- 내용
  - 코루틴을 스레드로 보내 실행시키는 객체 
  - 자신의 스레드 풀 존재함
    - 코루틴을 쉬고 있는 스레드로 보내서 실행
- 분류
  - Dispatchers.IO: 네트워크 요청이나 DB 읽기 쓰기 같은 입출력 작업을 실행하는 디스패쳐
  - Dispatchers.Default: CPU 바운드 작업을 위한 디스패처


<br>

## `CoroutineScope, CoroutineContext`

- CoroutineScope: 코루틴이 동작할 수 있는 영역
- CoroutineContext: 코루틴과 관련된 데이터를 보관

```kotlin
fun main() = runBlocking {
    // root coroutine
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        delay(1000L)
        println("Job 1")
    }

    // root coroutine
    val job2 = CoroutineScope(Dispatchers.Default).launch {
        delay(1000L)
        println("Job 2")
    }
}
```

