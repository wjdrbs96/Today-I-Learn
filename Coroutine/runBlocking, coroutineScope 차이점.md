# `runBlocking, coroutineScope 차이점`

## `runBlocking`

- runBlocking 내부에서 새로운 코루틴 스코프를 생성
- 블록 내부의 모든 코루틴이 끝날 때까지 runBlocking이 호출된 쓰레드는 대기
- 주로 main 함수와 같이 비코루틴에서 호출할 때나 테스트 코드에서 코루틴을 사용할 때 일반적인 코드 블록처럼 동작하게 하기 위해 사용됨

<br>

## `coroutineScope`

- suspend 함수 안에서만 호출할 수 있음
- coroutineScope는 새로운 코루틴 스코프를 만들지만 현재 쓰레드를 차단하지 않음

<br>

## `1. Coroutine 신규 생성 여부`

```kotlin
fun main() {
    runBlocking {
        println("[Main] ${Thread.currentThread().name}")
        makeCoroutine()
        notMakeCoroutine()
    }
}

fun makeCoroutine() = runBlocking {
    println("[RunBlocking] Make Coroutine ${Thread.currentThread().name}")
}

suspend fun notMakeCoroutine() = coroutineScope {
    println("[CoroutineScope] Make Coroutine ${Thread.currentThread().name}")
}
```
```
[Main] main @coroutine#1
[RunBlocking] Make Coroutine main @coroutine#2
[CoroutineScope] Make Coroutine main @coroutine#1
```

위의 코드를 보면 `runBlocking {}` 통해서 생성한 메소드는 코루틴을 새로 생성한 것을 볼 수 있고, `coroutineScope {}`을 통해서 생성한 메소드는 코루틴을 생성하지 않은 것을 볼 수 있습니다.

<br>

## `2. 스레드 블로킹 여부`

runBlocking {}은 현재 스레드를 블로킹하지만 coroutineScope {}은 suspend function 으로서 현재 스레드를 블로킹 하지 않는다는 차이가 있습니다.

스레드를 Blocking 하는지 안하는지가 가장 큰 차이라고 할 수 있는데, 처음에는 이 부분이 가장 잘 이해가 안되었습니다. 그래서 쓰레드와 코루틴에 대해 간단하게 먼저 정리해보려 합니다.

- 프로세스가 있어야만 스레드가 있을 수 있고, 스레드는 프로세스를 바꿀 수 없다.
- 코루틴의 코드가 실행되려면 스레드가 있어야 한다.
- 코루틴이 중단되었다가 재개될 때 다른 스레드에 배정될 수 있다.

쓰레드와 코루틴을 아주 간단하게 말하면 위와 같은 특징이 존재합니다.

<img width="1078" alt="스크린샷 2024-11-09 오전 12 23 41" src="https://github.com/user-attachments/assets/015b4fe8-db6c-402c-9c8d-cb9aa1fd364d">

하나의 코루틴은 여러 쓰레드에서 실행될 수 있다는 것을 표현하려고 대략적으로 그려보았습니다. 

그러면 여기서 쓰레드가 Blocking 되면 코루틴은 어떻게 될까요?

<img width="1372" alt="스크린샷 2024-11-09 오전 12 32 01" src="https://github.com/user-attachments/assets/497593b6-2ffc-40f7-a42f-4a4dea1a049a">

쓰레드가 멈추게 되었을 때 Blocking 되어 있으면 멈춰있는 시간 동안 다른 코루틴이 해당 쓰레드를 사용할 수 없게 됩니다.

반면에 Non-Blocking 이라면 쓰레드가 멈춰 있더라도 다른 코루틴이 해당 쓰레드를 사용하여 동작할 수 있습니다.

```kotlin
fun main() {
    runBlocking {
        blockingExample()
        nonBlockingExample()
    }
}

suspend fun blockingExample() = coroutineScope {
    repeat(2) { index ->
        launch {
            println("Blocking coroutine $index started on ${Thread.currentThread().name}")
            Thread.sleep(1000)  // Thread Block
            println("Blocking coroutine $index finished on ${Thread.currentThread().name}")
        }
    }
}

suspend fun nonBlockingExample() = coroutineScope {
    repeat(2) { index ->
        launch {
            println("Non-blocking coroutine $index started on ${Thread.currentThread().name}")
            delay(1000) // Thread Non-Block
            println("Non-blocking coroutine $index finished on ${Thread.currentThread().name}")
        }
    }
}
```
```
Blocking coroutine 0 started on main @coroutine#2
Blocking coroutine 0 finished on main @coroutine#2
Blocking coroutine 1 started on main @coroutine#3
Blocking coroutine 1 finished on main @coroutine#3
Non-blocking coroutine 0 started on main @coroutine#4
Non-blocking coroutine 1 started on main @coroutine#5
Non-blocking coroutine 0 finished on main @coroutine#4
Non-blocking coroutine 1 finished on main @coroutine#5
```

위의 코드를 보면 쓰레드 Block을 위해 `Thread.sleep`을 사용하였고, 쓰레드 Non-Block을 위해서 `suspend delay`를 사용하였습니다.

결과를 보면 Blocking 코드는 쓰레드가 Block 되기 때문에 순차 실행되는 것을 볼 수 있습니다.

반면에 Non-Blocking 코드는 쓰레드가 블락되지 않기 때문에 delay 통해서 코루틴이 멈춰있는 동안 새로운 코루틴이 생성되어 `Non-Blocking start`를 출력하는 것을 볼 수 있습니다. 

코드까지 보면 쓰레드가 Block, Non-Block이 어떤 차이가 있는지 알 수 있을 것입니다. 

이번에는 `runBlocking vs coroutineScope`을 사용해서 하나의 예시를 더 확인 해보겠습니다. 

<br>

### `예제 코드`

```kotlin
private val context = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

fun main() {
    runBlocking {
        blockingExample()
        println("***************************************************")
        nonBlockingExample()
    }
}

suspend fun blockingExample() = runBlocking {
    repeat(5) { index ->
        launch(context) {
            runBlocking {
                println("[A] Blocking coroutine $index started on ${Thread.currentThread().name}")
                delay(1000)
                println("[B] Blocking coroutine $index finished on ${Thread.currentThread().name}")
            }
        }
    }
}

suspend fun nonBlockingExample() = runBlocking {
    repeat(5) { index ->
        launch(context) {
            coroutineScope {
                println("[A] Non-blocking coroutine $index started on ${Thread.currentThread().name}")
                delay(1000)
                println("[B] Non-blocking coroutine $index finished on ${Thread.currentThread().name}")
            }
        }
    }
}
```
```
[A] Blocking coroutine 0 started on pool-1-thread-1 @coroutine#5
[A] Blocking coroutine 1 started on pool-1-thread-2 @coroutine#9
[B] Blocking coroutine 0 finished on pool-1-thread-1 @coroutine#5
[A] Blocking coroutine 2 started on pool-1-thread-1 @coroutine#10
[B] Blocking coroutine 1 finished on pool-1-thread-2 @coroutine#9
[A] Blocking coroutine 3 started on pool-1-thread-2 @coroutine#11
[B] Blocking coroutine 2 finished on pool-1-thread-1 @coroutine#10
[B] Blocking coroutine 3 finished on pool-1-thread-2 @coroutine#11
[A] Blocking coroutine 4 started on pool-1-thread-1 @coroutine#12
[B] Blocking coroutine 4 finished on pool-1-thread-1 @coroutine#12
***************************************************
[A] Non-blocking coroutine 1 started on pool-1-thread-1 @coroutine#15
[A] Non-blocking coroutine 0 started on pool-1-thread-2 @coroutine#14
[A] Non-blocking coroutine 2 started on pool-1-thread-2 @coroutine#16
[A] Non-blocking coroutine 3 started on pool-1-thread-1 @coroutine#17
[A] Non-blocking coroutine 4 started on pool-1-thread-2 @coroutine#18
[B] Non-blocking coroutine 1 finished on pool-1-thread-1 @coroutine#15
[B] Non-blocking coroutine 0 finished on pool-1-thread-2 @coroutine#14
[B] Non-blocking coroutine 2 finished on pool-1-thread-1 @coroutine#16
[B] Non-blocking coroutine 3 finished on pool-1-thread-2 @coroutine#17
[B] Non-blocking coroutine 4 finished on pool-1-thread-1 @coroutine#18
```

위의 코드는 2개의 쓰레드에서 Blocking(runBlocking), Non-Blocking(coroutineScope) 으로 동작하도록 코드를 작성한 것입니다. 

<img width="1246" alt="스크린샷 2024-11-09 오전 2 27 59" src="https://github.com/user-attachments/assets/ea9c58ec-17c9-4923-af2a-e947034a85dc">

먼저 Blocking 코드의 결과를 보면 위의 그림처럼 표현할 수 있습니다. runBlocking 안에서 `delay(1000)`를 통해서 코루틴이 중단 될 것인데요. `coroutine #1`에서 start, finish를 모두 출력하고 작업이 완료 되어야 다른 코루틴이 해당 쓰레드를 사용할 수 있도록 Block 될 것입니다.

즉, 해당 쓰레드는 중단되는 동안 다른 코루틴의 작업을 처리할 수 없다는 것을 의미합니다.

<br>

<img width="1277" alt="스크린샷 2024-11-09 오전 2 38 58" src="https://github.com/user-attachments/assets/22fe21f1-ea7f-41ac-94d9-fa9fb1b80367">

반면에 Non-Blocking 코드의 결과를 보면 coroutineScope 안에서 `delay(1000)`를 통해서 코루틴이 중단 될 때 Thread가 Block 되지 않고 다른 코루틴이 해당 쓰레드에서 실행되고 있는 것을 볼 수 있습니다.

<br>

## `Reference`

- [https://kghworks.tistory.com/205](https://kghworks.tistory.com/205)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html)
- [https://kotlinlang.org/docs/coroutines-basics.html#scope-builder](https://kotlinlang.org/docs/coroutines-basics.html#scope-builder)
- [https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-0-20176d431e9d](https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-0-20176d431e9d)
- [https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-1-98f6e792bd5b](https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-1-98f6e792bd5b)