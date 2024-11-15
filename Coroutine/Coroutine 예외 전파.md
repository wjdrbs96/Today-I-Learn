## `Coroutine 예외 전파`

- 코루틴 실행 도중 예외가 발생하면, 예외가 발생한 코루틴이 취소되고 예외가 부모 코루틴으로 전파된다.
- 만약 예외를 전파 받은 부모 코루틴도 예외를 적절히 처리하지 않는다면 취소되고, 그 상위의 코루틴으로 예외가 전파된다.

```kotlin
fun main() {
    runBlocking {
        launch(CoroutineName("Coroutine1")) {
            launch(CoroutineName("Coroutine3")) {
                throw Exception("예외 발생")
            }
            delay(100L)
            println("[${Thread.currentThread().name}] 코루틴 실행")
        }

        launch(CoroutineName("Coroutine2")) {
            delay(100L)
            println("[${Thread.currentThread().name}] 코루틴 실행")
        }

        delay(1000L)
    }
}
```
```
Exception in thread "main" java.lang.Exception: 예외 발생
	at com.example.coroutine.coroutine.ThreadAndCoroutineKt$main$1$1$1.invokeSuspend(ThreadAndCoroutine.kt:19)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	// 생략
```

위처럼 Coroutine1의 자식인 Coroutine3 에서 예외가 발생하니 부모까지 전파되어 runBlocking 코루틴도 영향을 받고, 그의 자식인 Coroutine2 에도 취소 전파를 영향을 받아 모두 취소되게 됩니다.

<br>

### `코루틴의 구조화를 깨서 예외 전파를 제한하기`

- 코루틴의 구조화를 깨면 예외 전파를 제한할 수 있다.
- 단순히 Job 객체를 새로 만들어 구조화를 깨고 싶은 코루틴에 연결하면 구조화가 깨진다.

