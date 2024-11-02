## `runBlocking, coroutineScope 차이점`

### `runBlocking`

- suspend 함수가 아니며 runBlocking은 비코루틴 코드를 실행하는 환경에서 코루틴을 사용할 수 있게 해줌
- runBlocking{} 블록은 주어진 블록이 완료될 때까지 현재 스레드를 멈추는 새로운 코루틴을 생성하여 실행하는 코루틴 빌더
- 코루틴 안에서는 runBlocking { } 의 사용은 권장되지 않으며, 일반적인 함수 코드 블록에서 중단 함수를 호출할 수 있도록 하기 위해서 존재하는 장치

<br>

### `coroutineScope`

- suspend 함수이기 때문에, 일반적인 함수나 비코루틴 코드를 실행하는 환경에서 사용할 수 없음
- 만일 어떤 코루틴들을 위한 사용자 정의 스코프가 필요한 경우가 있다면 coroutineScope{ } 빌더를 이용할 수 있음. 이 빌더를 통해 생성 된 코루틴은 모든 자식 코루틴들이 끝날때까지 종료되지 않는 스코프를 정의하는 코루틴
- 현재 스레드를 멈추지 않음

<br>

### `runBlocking vs coroutineScope`

두 빌더는 해당 스코프의 모든 코루틴이 종료될 때까지 최상위 코루틴이 블로킹되어 해당 코루틴이 다 끝나야 다음으로 넘어간다는 특징이 있습니다. 

```kotlin
fun main() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
}
```
```
Hello,
World!
```

```kotlin
fun main() = runBlocking {
    coroutineScope {
        launch {
            delay(1000L)
            println("World!")
        }
    }
    println("Hello,")
}
```
```
World!
Hello,
```

위에서 정리한 것처럼 runBlocking {}은 현재 스레드를 블로킹하지만 coroutineScope {}은 suspend function 으로서 현재 스레드를 릴리즈한다는 주요한 차이가 있습니다.

<br>

```kotlin
val testContext = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

fun main() = runBlocking {
    println("Main Start ${Thread.currentThread().name}")
    coroutineScope()
    
    runBlocking()
}

fun coroutineScope() = runBlocking {
    println("function Start ${Thread.currentThread().name}")
    (1..10).forEach {
        launch(testContext) {
            coroutineScope {
                println("Start No.$it in coroutineScope on ${Thread.currentThread().name}")
                delay(500)
                println("End No.$it in coroutineScope on ${Thread.currentThread().name}")
            }
        }
    }
}

fun runBlocking() = runBlocking {
    println("function Start ${Thread.currentThread().name}")
    (1..10).forEach {
        launch(testContext) {
            runBlocking {
                println("Start No.$it in runBlocking on ${Thread.currentThread().name}")
                delay(500)
                println("End No.$it in runBlocking on ${Thread.currentThread().name}")
            }
        }
    }
}
```
```
Main Start main @coroutine#1
function Start main @coroutine#2
Start No.2 in coroutineScope on pool-1-thread-2 @coroutine#4
Start No.1 in coroutineScope on pool-1-thread-1 @coroutine#3
Start No.4 in coroutineScope on pool-1-thread-2 @coroutine#6
Start No.3 in coroutineScope on pool-1-thread-1 @coroutine#5
Start No.5 in coroutineScope on pool-1-thread-2 @coroutine#7
Start No.7 in coroutineScope on pool-1-thread-2 @coroutine#9
Start No.6 in coroutineScope on pool-1-thread-1 @coroutine#8
Start No.8 in coroutineScope on pool-1-thread-2 @coroutine#10
Start No.9 in coroutineScope on pool-1-thread-1 @coroutine#11
Start No.10 in coroutineScope on pool-1-thread-2 @coroutine#12
End No.1 in coroutineScope on pool-1-thread-1 @coroutine#3
End No.2 in coroutineScope on pool-1-thread-2 @coroutine#4
End No.4 in coroutineScope on pool-1-thread-2 @coroutine#6
End No.3 in coroutineScope on pool-1-thread-1 @coroutine#5
End No.5 in coroutineScope on pool-1-thread-2 @coroutine#7
End No.7 in coroutineScope on pool-1-thread-1 @coroutine#9
End No.6 in coroutineScope on pool-1-thread-2 @coroutine#8
End No.8 in coroutineScope on pool-1-thread-1 @coroutine#10
End No.9 in coroutineScope on pool-1-thread-2 @coroutine#11
End No.10 in coroutineScope on pool-1-thread-1 @coroutine#12
===
function Start main @coroutine#13
Start No.1 in runBlocking on pool-1-thread-2 @coroutine#23
Start No.2 in runBlocking on pool-1-thread-1 @coroutine#22
End No.1 in runBlocking on pool-1-thread-2 @coroutine#23
Start No.3 in runBlocking on pool-1-thread-2 @coroutine#26
End No.2 in runBlocking on pool-1-thread-1 @coroutine#22
Start No.4 in runBlocking on pool-1-thread-1 @coroutine#27
End No.3 in runBlocking on pool-1-thread-2 @coroutine#26
Start No.5 in runBlocking on pool-1-thread-2 @coroutine#28
End No.4 in runBlocking on pool-1-thread-1 @coroutine#27
Start No.6 in runBlocking on pool-1-thread-1 @coroutine#29
End No.5 in runBlocking on pool-1-thread-2 @coroutine#28
Start No.7 in runBlocking on pool-1-thread-2 @coroutine#30
End No.6 in runBlocking on pool-1-thread-1 @coroutine#29
Start No.8 in runBlocking on pool-1-thread-1 @coroutine#31
End No.7 in runBlocking on pool-1-thread-2 @coroutine#30
Start No.9 in runBlocking on pool-1-thread-2 @coroutine#32
End No.8 in runBlocking on pool-1-thread-1 @coroutine#31
Start No.10 in runBlocking on pool-1-thread-1 @coroutine#33
End No.9 in runBlocking on pool-1-thread-2 @coroutine#32
End No.10 in runBlocking on pool-1-thread-1 @coroutine#33
```




<br>

## `Reference`

- [https://kghworks.tistory.com/205](https://kghworks.tistory.com/205) => 정리 아주 잘되어 있음
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html)
- [https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/coroutine-scope.html)
- [https://kotlinlang.org/docs/coroutines-basics.html#scope-builder](https://kotlinlang.org/docs/coroutines-basics.html#scope-builder)
- [https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-0-20176d431e9d](https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-0-20176d431e9d)
- [https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-1-98f6e792bd5b](https://myungpyo.medium.com/reading-coroutine-official-guide-thoroughly-part-1-98f6e792bd5b)