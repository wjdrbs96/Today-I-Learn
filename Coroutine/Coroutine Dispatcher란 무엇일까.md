# `Coroutine Dispatcher란 무엇일까?`

```
Coroutine(코루틴) + Dispatcher (보내는 주체)
```

코루틴을 스레드로 보내야 실행할 수 있습니다. 즉, `Coroutine Dispatcher`는 코루틴을 스레드로 보내 실행시키는 객체라고 할 수 있습니다.

<br>

## `CoroutineDispatcher`

### `Dispatcher.IO`

네트워크 요청이나 DB 읽기 쓰기 같은 입출력(I/O) 작업을 실행하는 디스패쳐

<img width="639" alt="스크린샷 2024-11-06 오후 11 00 30" src="https://github.com/user-attachments/assets/8ee155b6-52f3-4bc6-8f85-1b908412b3c6">

```kotlin
internal object DefaultIoScheduler : ExecutorCoroutineDispatcher(), Executor {

    private val default = UnlimitedIoScheduler.limitedParallelism(
        systemProp(
            IO_PARALLELISM_PROPERTY_NAME,
            64.coerceAtLeast(AVAILABLE_PROCESSORS)
        )
    )
}
```

사용할 수 있는 스레드 수: 64와 JVM에서 사용할 수 있는 프로세서의 수 중 큰 값

<br>

### `Dispatcher.Default` 

<img width="646" alt="스크린샷 2024-11-06 오후 11 01 41" src="https://github.com/user-attachments/assets/a111b027-18a3-4a59-b156-9a30a5a39410">

CPU 바운드 작업을 위한 디스패쳐 (ex: 이미지, 동영상 처리나 대용량 데이터 변환 같은 끊이지 않고 연산이 필요한 작업)

CPU 바운드 작업을 위해 I/O 작업과 같은 쓰레드 풀을 사용하면 CPU 바운드 작업이 모든 쓰레드를 사용하면 I/O 작업을 할 수 없습니다. CPU 바운드 작업을 처리하기 위하여 별도의 디스패쳐를 만들었습니다.

```kotlin
@JvmField
internal val CORE_POOL_SIZE = systemProp(
    "kotlinx.coroutines.scheduler.core.pool.size",
    AVAILABLE_PROCESSORS.coerceAtLeast(2),
    minValue = CoroutineScheduler.MIN_SUPPORTED_POOL_SIZE
)
```

쓰레드 수는 각자 사용하는 머신의 프로세서 만큼 사용되고 최소 2개 입니다.

