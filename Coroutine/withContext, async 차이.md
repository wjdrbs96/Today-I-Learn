## `withContext, async 차이`

### `withContext`

인자로 받은 CoroutineContext를 사용해 코루틴의 실행 스레드를 전환하고, 람다식의 코드를 실행한 후 결과값을 반환하는 함수

람다식을 실행한 후에는 스레드가 다시 이전의 Dispatcher를 사용하도록 전환된다.

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

### `async-await`

async 코루틴 빌더를 호출하면 코루틴이 생성되고, `Deffered<T>` 타입의 객체가 반환됩니다. Deffered는 Job과 같이 코루틴을 추상화한 객체이지만, 코루틴으로부터 생성된 결과값을 감싸는 기능을 추가로 가집니다.

async 함수가 launch 함수와 다른 점은 block 람다식이 제네릭(T)을 반환한다는 점과 반환 객체가 `Deffered<T>` 라는 것입니다.

```kotlin
fun main() = runBlocking {
    val networkDeffered = async(Dispatchers.IO) {
        delay(1000L)
        return@async  "Dummy Response"
    }


    val result = networkDeffered.await() // networkDeffered 로부터 결과 값이 반환될 때까지 runBlocking 일시 중단
    println("result: $result") 
}
```
```
result: Dummy Response
```

<img width="657" alt="스크린샷 2024-11-06 오후 9 57 42" src="https://github.com/user-attachments/assets/853b99eb-e78b-409c-83a7-6dd078af9a55">

<br>

### `withConext와 async-await의 차이`

async-await이 연속으로 호출되는 동작을 withContext로 대체 가능하다.

```kotlin
fun main() = runBlocking {
    val result = async(Dispatchers.IO) {
        delay(2000L)
        println("${Thread.currentThread().name} 결과값이 반환됩니다.")
        return@async "결과값"
    }.await()

    println("${Thread.currentThread().name} 결과값이 반환됩니다. result: $result")
}
```
```
DefaultDispatcher-worker-1 @coroutine#2 결과값이 반환됩니다.
main @coroutine#1 결과값이 반환됩니다. result: 결과값
```

withContext를 사용하나 async-await을 사용하나 결과가 동일하여 같은 것처럼 보일 수 있지만, 결과를 보면 코루틴이 다른 것을 볼 수 있습니다. (coroutine 1, 2)

즉, withContext, async-await 모두 순차처리는 되지는 하나의 코루틴에서 처리하는지, 새로운 코루틴을 생성하냐 처럼 완전히 다르게 동작합니다.

<br>

## `withConext 사용시 주의점`

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
```
main @coroutine#1, 합쳐진 결과값: 결과값1, 결과값2
result: 2045 ms
```

위의 코드를 보면 병렬로 실행되어 1초 살짝 넘어서 끝날거 같지만, 실재로는 2초 조금 넘게 걸리는 것을 볼 수 있습니다.

이유는 위에서 본 것처럼 withContext는 새로운 코루틴을 생성하는 것이 아니기 때문에 하나씩 순차 처리되기 때문입니다.

<br>

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
```
main @coroutine#1, 합쳐진 결과값: 결과값1, 결과값2
result: 1017 ms
```

이럴 때는 `async-await`을 사용하여 새로운 코루틴을 생성하여 병렬 처리를 진행할 수 있습니다.

<br>

### `withContext를 사용한 코루틴 스레드 전환`

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

## `Reference`

- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/with-context.html)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/async.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/async.html)