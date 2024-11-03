## `Coroutine launch`

```kotlin
fun main() = runBlocking {
    val followingList = listOf("1", "2", "3")

    val result = measureTimeMillis {
        followingList.map {
            launch {
                delay(1000L)
                printWithThread("item $it")
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

- map을 진행하면서 코루틴을 생성하여 동시에 처리하여 1초 정도 소요


```kotlin
fun main() = runBlocking {
    val followingList = listOf("1", "2", "3")
    
    val result = measureTimeMillis {
        followingList.map {
            Thread.sleep(1000L)
            printWithThread("item $it")
        }
    }

    println("result: $result")
}
```
```
main @coroutine#1 item 1
main @coroutine#1 item 2
main @coroutine#1 item 3
result: 3019
```

- map을 진행하면서 순차적으로 1초씩 멈추기 때문에 전체 3초 정도 소요