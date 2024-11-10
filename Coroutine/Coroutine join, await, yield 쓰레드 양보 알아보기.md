## `Coroutine join, await, yield 쓰레드 양보 알아보기`

- Job의 join 함수나 Deffered의 await 함수가 호출되면 해당 함수를 호출한 코루틴은 쓰레드를 양보하고 join 또는 await의 대상이 된 코루틴을 완전히 종료할 때까지 일시 중단된다.
- 하나의 코루틴이 쓰레드를 양보하지 않으면 다른 코루틴은 쓰레드를 점유하지 못한다.

```kotlin
fun main() = runBlocking {
    val job = launch {
        println("1. 코루틴의 작업이 실행 되었습니다.")
        delay(1000L)
        println("2. 코루틴의 작업이 완료 되었습니다.")
    }

    println("3. runBlocking 코루틴이 곧 일시 중단되고 메인 스레드가 양보합니다.")
    job.join()
    println("4. runBlocking 이 Main 쓰레드로 보내져 작업을 재개합니다.")
}
```
```
3. runBlocking 코루틴이 곧 일시 중단되고 메인 스레드가 양보합니다.
1. 코루틴의 작업이 실행 되었습니다.
2. 코루틴의 작업이 완료 되었습니다.
4. runBlocking 이 Main 쓰레드로 보내져 작업을 재개합니다.
```

<img width="1142" alt="스크린샷 2024-11-09 오후 7 25 04" src="https://github.com/user-attachments/assets/14cb3d53-e932-4576-a194-8fa68287b61b">

먼저 runBlocking 코루틴이 생성되어 메인 스레드를 사용하기 때문에 3번이 먼저 출력됩니다. 그리고 `job.join` 함수를 만났기 때문에 4번을 출력하지 않고 `launch 코루틴에게 쓰레드를 양보`합니다. 그리고 `launch 코루틴에서도 delay(1000L)을 통해서 1초 쓰레드 양보하지만, join 함수는 launch 코루틴 작업이 모두 끝날 때까지 대기하는 것이기 때문에 대기하게 됩니다.` 

<br>

## `yield 함수 쓰레드 양보`

```kotlin
fun main() = runBlocking {
    val job = launch {
        while (this.isActive) {
            println("작업 중")
        }
    }

    delay(100L)
    job.cancel()
}
```
```
작업 중
작업 중
작업 중
.. 무한 반복
```

<img width="1025" alt="스크린샷 2024-11-09 오후 7 41 35" src="https://github.com/user-attachments/assets/283f67d1-789e-4dd8-bc3f-53c44b712391">

위의 코드는 무한 반복으로 출력되게 됩니다. 먼저 runBlocking 코루틴이 메인 쓰레드를 사용하였지만, `delay(100L)` 통해서 쓰레드를 양보하여, launch 코루틴이 메인 쓰레드를 사용하게 됩니다. 하지만 launch 코루틴은 쓰레드를 양보하는 코드가 없기 때문에 양보하지 않고 계속 혼자 코루틴을 사용하게 됩니다.

<br>

```kotlin
fun main() {
    runBlocking {
        val whileJob = launch(Dispatchers.Default) {
            while (this.isActive) {
                println("${Thread.currentThread().name}, 작업 중")
            }
        }

        delay(100L)
        whileJob.cancel()
    }
}
```
```
DefaultDispatcher-worker-1, 작업 중
DefaultDispatcher-worker-1, 작업 중
DefaultDispatcher-worker-1, 작업 중
...

Process finished with exit code 0
```

<img width="1139" alt="스크린샷 2024-11-09 오후 7 44 33" src="https://github.com/user-attachments/assets/1bc3c385-2b8f-42a8-901e-33c46a031f69">

이번에는 `launch 코루틴에 Dispatcher를 지정해줬기 때문에 Main 쓰레드를 사용하는 것이 아니라 백그라운드 쓰레드를 사용하게 됩니다.` 즉, runBlocking 코루틴은 100 밀리초 후에 메인 쓰레드를 다시 사용하여 `whileJob.cancel()`을 호출하여 백그라운 쓰레드를 사용하고 있는 launch 코루틴을 중단시킬 수 있습니다.

<br>

### `코루틴의 실행 쓰레드는 고정이 아니다.`

- 코루틴이 일시 중단된 후 재개되면 CoroutineDispatcher 객체는 재개된 코루틴을 다시 쓰레드에 보낸다.
- CoroutineDispatcher 객체는 코루틴을 사용할 수 있는 쓰레드 중 하나에 보내기 때문에, 코루틴이 재개된 후의 실행 쓰레드는 중단 되기 전 쓰레드와 다를 수 있다.

