## `Coroutine Blocking, Non-Blocking`

### `루틴과 코루틴`

```kotlin
fun main() {
    runBlocking {
        println("START")
        launch {
            newRoutineExample()
        }
        yield()
        println("END")
    }
}

suspend fun newRoutineExample() {
    val num1 = 1
    val num2 = 2
    yield()
    println("${num1 + num2}")
}
```

1. Main 코루틴 실행
2. Main 코루틴 중단
3. 자식 코루틴 실
4. 자식 코루틴 중단
5. Main 코루틴 종료
6. 자식 코루틴 종료

코루틴은 재개, 중단 과정이 존재합니다. 그래서 새로운 루틴이 호출된 후 완전히 종료되기 전, `해당 루틴에서 사용했던 정보들을 보관`하고 있어야 합니다.
  
즉, 루틴이 중단되었다가 다시 해당 메모리에 접근이 가능합니다.

<br>

### `스레드와 코루틴`

- 프로세스가 있어야만 스레드가 있을 수 있고, 스레드는 프로세스를 바꿀 수 없다.
- 코루틴의 코드가 실행되려면 스레드가 있어야 한다.
  - 하지만 중단되었다가 재개될 때 다른 스레드에 배정될 수 있다.

<br>

### `예제 코드`

```kotlin
fun main() = runBlocking {
    println("=== Blocking Example ===")
    val blockingTime = measureTimeMillis {
        blockingExample()
    }

    println("Blocking code took $blockingTime ms\n")

    println("=== Non-Blocking Example ===")
    val nonBlockingTime = measureTimeMillis {
        nonBlockingExample()
    }
    println("Non-blocking code took $nonBlockingTime ms")
}

// 블로킹 코드 예시
suspend fun blockingExample() = coroutineScope {
    repeat(5) { index ->
        launch {
            println("Blocking coroutine $index started on ${Thread.currentThread().name}")
            Thread.sleep(1000) // 블로킹 코드
            println("Blocking coroutine $index finished on ${Thread.currentThread().name}")
        }
    }
}

// 비차단 코드 예시
suspend fun nonBlockingExample() = coroutineScope {
    repeat(5) { index ->
        launch {
            println("Non-blocking coroutine $index started on ${Thread.currentThread().name}")
            delay(1000) // 비차단 코드
            println("Non-blocking coroutine $index finished on ${Thread.currentThread().name}")
        }
    }
}
```
```
=== Blocking Example ===
Blocking coroutine 0 started on main @coroutine#2
Blocking coroutine 0 finished on main @coroutine#2
Blocking coroutine 1 started on main @coroutine#3
Blocking coroutine 1 finished on main @coroutine#3
Blocking coroutine 2 started on main @coroutine#4
Blocking coroutine 2 finished on main @coroutine#4
Blocking coroutine 3 started on main @coroutine#5
Blocking coroutine 3 finished on main @coroutine#5
Blocking coroutine 4 started on main @coroutine#6
Blocking coroutine 4 finished on main @coroutine#6
Blocking code took 5029 ms

=== Non-Blocking Example ===
Non-blocking coroutine 0 started on main @coroutine#7
Non-blocking coroutine 1 started on main @coroutine#8
Non-blocking coroutine 2 started on main @coroutine#9
Non-blocking coroutine 3 started on main @coroutine#10
Non-blocking coroutine 4 started on main @coroutine#11
Non-blocking coroutine 0 finished on main @coroutine#7
Non-blocking coroutine 1 finished on main @coroutine#8
Non-blocking coroutine 2 finished on main @coroutine#9
Non-blocking coroutine 3 finished on main @coroutine#10
Non-blocking coroutine 4 finished on main @coroutine#11
Non-blocking code took 1007 ms
```

위의 코드를 보면 Blocking 코드는 Start -> Finish 번갈아 출력되는 반면에 Non-Blocking 코드는 start가 모두 출력된 후에 finish가 출력되는 것을 볼 수 있습니다.

- Blocking: Thread가 멈추기 때문에 `launch {}` 안에 코드가 모두 실행된 후에 다음 코루틴이 진행되는 것임 (그래서 순차처리로 보임)
- Non-Blocking: Thread가 멈추지 않기 때문에 `launch {}`가 반복되면서 여러 개의 코루틴이 생성되어 실행되기 때문에 finish가 출력되기 전에 start가 모두 출력될 수 있는 것임