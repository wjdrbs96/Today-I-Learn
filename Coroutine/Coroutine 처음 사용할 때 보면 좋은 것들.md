## `Coroutine 처음 사용할 때 보면 좋은 것들`

- [루틴과 코루틴]()
  - [루틴]()
  - [코루틴]()
- [Blocking, Non-Blocking]()
  - [스레드와 코루틴]()
  - [예제 코드]()
- [CoroutineDispatcher]()
- [코루틴 빌더]()
  - [launch]()
  - [runBlocking, coroutineScope]()
    - [신규 코루틴 생성 여부]()
    - [스레드 Block 여부]()
  - [async]()
  - [withContext]()
    - [withContext 사용시 주의점]()
    - [withContext를 사용한 코루틴 스레드 전환]()
  - [withConext와 async 차이]()
- [Blocking, Non-Blocking]()
- [코루틴 일시 중단 함수란?]()
  - [일시 중단 함수의 오해]()
  - [일시 중단 함수를 별도 코루틴에서 실행하기]()
  - [일시 중단 함수끼리 코루틴 실행]()
  - [일시 중단 함수에서 코루틴 빌더 실행하기]()

<br>

## `루틴과 코루틴`

### `루틴`

- 새로운 루틴(함수)이 호출되면 사용되는 스택에 지역 변수 초기화
- 루틴(함수)이 끝나면 해당 메모리에 접근 불가능
- 루틴에 진입하는 곳이 한 군데이며, 종료되면 해당 루틴의 정보가 초기화

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

- `새로운 루틴이 호출된 후 완전히 종료되기 전, 해당 루틴에서 사용했던 정보들을 보관하고 있어야 함` ⭐⭐
  - 코루틴은 중단, 재개라는 개념이 존재함
  - 루틴이 중단되었다가 재개될 때 다시 해당 메모리에 접근이 가능함
- runBlocking: 코루틴 생성
- launch: 코루틴 생성
- yield(): 지금 코루틴을 중단하고 다른 코루틴이 실행되도록 함

```kotlin
fun main() = runBlocking {    // 부모 코루틴
    println("START, Thread: ${Thread.currentThread().name}")
    launch {                  // 자식 코루틴
        newRoutine1()
    }

    yield()                   // 부모 코루틴 중단
    println("END, Thread: ${Thread.currentThread().name}")
}

suspend fun newRoutine1() {
    val num1 = 1
    val num2 = 2

    yield()                   // 자식 코루틴 중단
    println("${num1 + num2}, Thread: ${Thread.currentThread().name}")
}
```

Main 스레드 하나에서 여러 개의 코루틴이 중단 되었다가 재개되어 실행되는 것을 확인할 수 있음

<br>

### `스레드와 코루틴`

- 스레드
  - 프로세스보다 작은 개념
  - context switching 발생시 Heap 메모리를 공유하고, Stack만 교체되므로 프로세스 보다 비용이 적다.
  - OS가 스레드를 강제로 멈추고 다른 스레드를 실행한다.
- 코루틴
  - 스레드보다 작은 개념이라 경량 스레드라고 불림
  - `한 코루틴은 여러 스레드에서 실행될 수 있다.` ⭐⭐
  - 한 스레드에서 실행하는 경우 context switching 발생시 메모리 교체가 없다.
  - 코루틴 스스로가 다른 코루틴에게 양보한다.

<br>

## `CoroutineDispatcher`

- 내용
  - 코루틴의 실행을 관리하는 객체
  - 코루틴을 스레드로 보내 실행시키는 객체
  - 자신의 스레드 풀 존재함
  - 코루틴을 쉬고 있는 스레드로 보내서 실행
- 분류
  - Dispatchers.IO: 네트워크 요청이나 DB 읽기 쓰기 같은 입출력 작업을 실행하는 디스패쳐
  - Dispatchers.Default: CPU 바운드 작업을 위한 디스패처

<img width="886" alt="스크린샷 2024-11-11 오후 10 51 34" src="https://github.com/user-attachments/assets/d5647e11-2430-4e11-a3db-52ed6fb1d533">

<br>

## `코루틴 빌더`

### `launch` ([Link](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/launch.html))

- 새로운 코루틴을 시작하는 코루틴 빌더
- 반환 값이 없는 코드를 실행 

```kotlin
fun main() = runBlocking {  // 부모 코루틴
    val followingList = listOf("1", "2", "3")

    val result = measureTimeMillis {
        followingList.map {
            launch {   // 순회를 돌면서 코루틴이 하나씩 생성됨
                delay(1000L)
                println("item: $it, Thread: ${Thread.currentThread().name}")
            }
        }.joinAll()
    }

    println("result: $result")
}
```
```
main @coroutine#2 item 1
main @coroutine#3 item 2
main @coroutine#4 item 3
result: 1020
```

map을 진행하면서 코루틴을 생성하여 동시에 처리하여 1초 정도 소요

<img width="1150" alt="스크린샷 2024-11-13 오후 10 52 44" src="https://github.com/user-attachments/assets/591ae0eb-be31-45a9-bbd6-8d348030bc73">

<br>

```kotlin
fun main() = runBlocking {
    val followingList = listOf("1", "2", "3")

    val result = measureTimeMillis {
        followingList.map {
        launch {
            Thread.sleep(1000L)
            println("item $it, Thread: ${Thread.currentThread().name}")
        }
        }.joinAll()
    }

    println("result: $result")
}
```
```
item 1, Thread: main @coroutine#2
item 2, Thread: main @coroutine#3
item 3, Thread: main @coroutine#4
result: 3036
```

- map을 진행하면서 순차적으로 1초씩 멈추기 때문에 전체 3초 정도 소요

<img width="1305" alt="스크린샷 2024-11-13 오후 10 56 49" src="https://github.com/user-attachments/assets/96388c04-54c7-41c2-bad4-04a014d2f9a9">

<br>

### `runBlocking, coroutineScope` ([runBlocking](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html), [coroutineScope](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html))

- runBlocking
  - 새로운 코루틴을 만들고 루틴과 코루틴을 연결해주는 코루틴 빌더
  - 신규 코루틴을 생성함
  - runBlocking 내부에서 실행되는 Coroutine이 모두 완료될 때까지 쓰레드를 Block 시킴
- coroutineScope
  - suspend 함수 안에서만 호출할 수 있음
  - 신규 코루틴을 생성하지 않음
  - coroutineScope는 새로운 코루틴 스코프를 만들지만 현재 쓰레드를 차단하지 않음

<br>

#### `신규 코루틴 생성 여부`

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
<details>
<summary>결과</summary>

```
[Main] main @coroutine#1
[RunBlocking] Make Coroutine main @coroutine#2
[CoroutineScope] Make Coroutine main @coroutine#1
```
</details>

<br>
 
#### `스레드 블락 여부` ⭐⭐⭐⭐

- runBlocking: 스레드 Block 시킴
- coroutineScope: 스레드 Block 시키지 않음

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
            runBlocking { // Thread Block
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
            coroutineScope { // Thread Non-Block
                println("[A] Non-blocking coroutine $index started on ${Thread.currentThread().name}")
                delay(1000)
                println("[B] Non-blocking coroutine $index finished on ${Thread.currentThread().name}")
            }
        }
    }
}
```
<details>
<summary>결과</summary>

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
</details>

![image](https://github.com/user-attachments/assets/4bdab21f-6dd5-4b81-812c-221b1e863a7e)

- 스레드 Block

<br>

![image](https://github.com/user-attachments/assets/0590cd0a-1779-4c8a-9e1e-6d4e6ac4a9a7)

- 스레드 Non-Block

<br>

<br>

### `async` ([Link](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/async.html))

- async 코루틴 빌더를 호출하면 코루틴이 생성되고, `Deffered<T>` 타입의 객체가 반환됨
- Deffered는 Job과 같이 코루틴을 추상화한 객체이지만, 코루틴으로부터 생성된 결과값을 감싸는 기능을 추가로 가짐
- async 함수가 launch 함수와 다른 점은 block 람다식이 제네릭(T)을 반환한다는 점과 반환 객체가 `Deffered<T>` 라는 것

```kotlin
fun main() = runBlocking {
  val networkDeffered = async(Dispatchers.IO) {
    println("Async Thread: ${Thread.currentThread().name}")
    delay(1000L)
    return@async "Dummy Response"
  }


  val result = networkDeffered.await() // networkDeffered 로부터 결과 값이 반환될 때까지 runBlocking 일시 중단
  println("result: $result, Thread: ${Thread.currentThread().name}")
}
```
```
Async Thread: DefaultDispatcher-worker-1 @coroutine#2   # 신규 코루틴 추가된 것을 볼 수 있음
result: Dummy Response, Thread: main @coroutine#1  
```

<img width="657" alt="스크린샷 2024-11-06 오후 9 57 42" src="https://github.com/user-attachments/assets/853b99eb-e78b-409c-83a7-6dd078af9a55">

<br>

### `withContext` ([Link](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html))

- 인자로 받은 CoroutineContext를 사용해 코루틴의 실행 스레드를 전환하고, 람다식의 코드를 실행한 후 결과값을 반환하는 함수
- 람다식을 실행한 후에는 스레드가 다시 이전의 Dispatcher를 사용하도록 전환됨

```kotlin
fun main() = runBlocking {
    val result = withContext(Dispatchers.IO) {
        delay(2000L)
        println("${Thread.currentThread().name} 결과값이 반환됩니다.")
        "결과값"
    }

    println("${Thread.currentThread().name} 결과값이 반환됩니다. result: $result")
}
```
```
DefaultDispatcher-worker-1 @coroutine#1 결과값이 반환됩니다.
main @coroutine#1 결과값이 반환됩니다. result: 결과값
```

결과를 보면 실행 쓰레드는 다르고(I/O, Main Thread) 코루틴(`coroutine#1`)은 같은 것을 볼 수 있습니다.

즉, withContext는 새로운 코루틴을 실행하는 것이 아니라 코루틴의 특정 블록에 다른 Dispatcher를 실행하도록 만들고 결과 값을 받고 다시 원래의 Dispatcher를 사용하도록 하는 함수입니다.

<img width="688" alt="스크린샷 2024-11-06 오후 9 58 37" src="https://github.com/user-attachments/assets/03085589-30d4-436b-a0cf-f0c011261f48">

<br>

#### `withContext 사용시 주의점`

- 잘못된 사용 코드

```kotlin
fun main() = runBlocking {
    val result = measureTimeMillis {
        val result1 = withContext(Dispatchers.IO) {
            delay(1000L)
            "결과값1"
        }

        val result2 = withContext(Dispatchers.IO) {
            delay(1000L)
            "결과값2"
        }

        val results = listOf(result1, result2)
        println("${Thread.currentThread().name}, 합쳐진 결과값: ${results.joinToString(", ")}")
    }

    println("result: $result")
}
```
<details>
<summary>결과</summary>

```
main @coroutine#1, 합쳐진 결과값: 결과값1, 결과값2
result: 2045 ms
```

위의 코드를 보면 병렬로 실행되어 1초 살짝 넘어서 끝날거 같지만, 실재로는 2초 조금 넘게 걸리는 것을 볼 수 있습니다.

이유는 위에서 본 것처럼 withContext는 새로운 코루틴을 생성하는 것이 아니기 때문에 하나씩 순차 처리되기 때문입니다.
</details>

<br>

- 올바른 사용 코드

```kotlin
fun main() = runBlocking {
    val result = measureTimeMillis {
        val result1 = async(Dispatchers.IO) {
            delay(1000L)
            "결과값1"
        }

        val result2 = async(Dispatchers.IO) {
            delay(1000L)
            "결과값2"
        }

        val results = awaitAll(result1, result2)
        println("${Thread.currentThread().name}, 합쳐진 결과값: ${results.joinToString(", ")}")
    }

    println("result: $result ms")
}
```
<details>
<summary>결과</summary>

```
main @coroutine#1, 합쳐진 결과값: 결과값1, 결과값2
result: 1017 ms
```

병렬 처리를 해야 한다면 `async-await`을 사용하여 새로운 코루틴을 생성하여 진행하면 됩니다.

</details>

<br>

#### `withContext를 사용한 코루틴 스레드 전환`

```kotlin
private val dispatcher1 = newSingleThreadContext("MyThread1")
private val dispatcher2 = newSingleThreadContext("MyThread2")

fun main() = runBlocking {
    println("${Thread.currentThread().name} 코루틴 실행1")
    withContext(dispatcher1) {
        println("${Thread.currentThread().name} 코루틴 실행2")
        withContext(dispatcher2) {
            println("${Thread.currentThread().name} 코루틴 실행3")
        }

        println("${Thread.currentThread().name} 코루틴 실행4")
    }

    println("${Thread.currentThread().name} 코루틴 실행5")
}
```
```
main @coroutine#1 코루틴 실행1
MyThread1 @coroutine#1 코루틴 실행2
MyThread2 @coroutine#1 코루틴 실행3
MyThread1 @coroutine#1 코루틴 실행4
main @coroutine#1 코루틴 실행5
```

withConext는 자신이 속한 블록 내부에서 coroutine의 context인 coroutine을 실행하는 coroutineDispatcher를 바꾸는데 사용합니다.

<br>

### `withConext와 async 차이`

- async-await이 연속으로 호출되는 동작을 withContext로 대체 가능
- withContext를 사용하나 async-await을 사용하나 결과가 동일하여 같은 것처럼 보일 수 있지만, 결과를 보면 코루틴이 다른 것을 볼 수 있음 (coroutine 1, 2)
- withContext, async-await 모두 순차처리는 되지만 하나의 코루틴에서 처리하는지, 새로운 코루틴을 생성하냐 차이로 완전히 다르게 동작함

<br>

## `Blocking, Non-Blocking`

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
<details>
<summary>결과</summary>

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
</details>

<br>

## `코루틴 일시 중단 함수란?`

- 일시 중단 함수란 suspend 키워드로 선언되는 함수로 함수 내에 `일시 중단 지점`을 포함할 수 있는 함수
- 일시 중단 지점이 포함될 수 있기 때문에 코루틴에서만 호출 가능

<br>

### `일시 중단 함수의 오해`

- 일시 중단 함수는 코루틴이 아니다.
- 단순히 일시 중단 지점을 포함할 수 있는 함수이다.

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    delayAndPrintHelloWorld()
    delayAndPrintHelloWorld()
    println(getElapsedTime(startTime))
}

suspend fun delayAndPrintHelloWorld() {
    delay(1000L)
    println("Hello World")
}

fun getElapsedTime(startTime: Long): String = "소요 시간: ${System.currentTimeMillis() - startTime} ms"
```
```
Hello World
Hello World
지난 시간: 2014 ms
```

위의 코드를 보면 코루틴이 여러 개 실행되어 1초 정도에 완료될 것이라 생각할 수도 있는데요. 하지만 runBlocking을 통해 생성되는 코루틴 외에 suspend 함수인 delayAndPrintHelloWorld을 호출 했을 때 코루틴은 생성되지 않기 때문에 2초 살짝 넘게 걸리는 것을 볼 수 있습니다.

<br>

### `일시 중단 함수를 별도 코루틴에서 실행하기`

- 일시 중단 함수를 별도의 코루틴에서 실행하려면 일시 중단 함수 호출부를 별도의 코루틴 빌더로 감싸야 함

```kotlin
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job1 = launch {
        delayAndPrintHelloWorld()
    }

    val job2 = launch {
        delayAndPrintHelloWorld()
    }

    joinAll(job1, job2)
    println(getElapsedTime(startTime))
}

suspend fun delayAndPrintHelloWorld() {
    delay(1000L)
    println("Hello World")
}

fun getElapsedTime(startTime: Long): String = "소요 시간: ${System.currentTimeMillis() - startTime} ms"
```
```
Hello World
Hello World
소요 시간: 1014 ms
```

별도의 코루틴을 생성하여 일시 중단 함수를 호출하도록 변경한 후에 실행하니 1초 정도 소요된 것을 볼 수 있습니다.

<br>

### `일시 중단 함수끼리 코루틴 실행`

```kotlin
suspend fun searchByWord(keyword: String): Array<String> {
    val dbResultsDeffered = searchFromDb(keyword)
    val serverResultsDeffered = searchFromDb(keyword)
    return arrayOf(*dbResultsDeffered, *serverResultsDeffered)
}

suspend fun searchFromDb(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[DB] ${keyword}1, [DB] ${keyword}2")
}

suspend fun searchFromServer(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[Server] ${keyword}1, [Server] ${keyword}2")
}
```

그리고 일시 중단 함수에서도 호출할 수 있는 것을 볼 수 있습니다. 하지만 위의 코드도 코루틴이 하나이기 때문에 순차적으로 동작한다는 특징이 있습니다.

그래서 코루틴 빌더인 async 함수를 통해서 호출하고자 하는데 오류가 발생하게 됩니다. 이유는 일시 중단 함수에서는 coroutine scope에 접근할 수 없기 때문입니다. (async는 CoroutineScope의 확장 함수로 선언되어 있기 때문임)

<br>

### `일시 중단 함수에서 코루틴 빌더 실행하기`

- coroutineScope 함수를 사용하면 일시 중단 함수 내부에서 새로운 CoroutineScope 함수를 호출할 수 있다.

```kotlin
suspend fun searchByKeyWord(keyword: String): Array<String> = coroutineScope {
    val dbResultsDeffered = async {
        searchFromDb(keyword)
    }

    val serverResultsDeffered = async {
        searchFromServer(keyword)
    }

    return@coroutineScope arrayOf(*dbResultsDeffered.await(), *serverResultsDeffered.await())
}

suspend fun searchFromDb(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[DB] ${keyword}1, [DB] ${keyword}2")
}

suspend fun searchFromServer(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[Server] ${keyword}1, [Server] ${keyword}2")
}
```
```
[결과], [[DB] Keyword1, [DB] Keyword2, [Server] Keyword1, [Server] Keyword2]
소요 시간: 1033 ms
```

<br>

## `Reference`

- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/launch.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/launch.html)
- [https://kghworks.tistory.com/205](https://kghworks.tistory.com/205)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html)
- [https://kotlinlang.org/docs/coroutines-basics.html#scope-builder](https://kotlinlang.org/docs/coroutines-basics.html#scope-builder)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html)