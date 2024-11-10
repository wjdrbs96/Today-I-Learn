## `Coroutine 일시 중단 함수란?`

- 일시 중단 함수란 suspend 키워드로 선언되는 함수로 함수 내에 `일시 중단 지점`을 포함할 수 있는 함수
- 일시 중단 함수는 코루틴의 일시 중단 지점이 포함된 코드를 재사용할 수 있는 코드의 집합으로 만드는데 사용
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

위의 코드를 보면 코루틴이 여러 개 실행되어 1초 약간 더 걸려서 완료될 것이라 생각할 수도 있는데요. 하지만 runBlocking을 통해 생성되는 코루틴 외에 suspend 함수인 delayAndPrintHelloWorld을 호출 했을 때 코루틴은 생성되지 않기 때문에 2초 살짝 넘게 걸리는 것을 볼 수 있습니다.

<br>

### `일시 중단 함수를 별도 코루틴에서 실행하기`

일시 중단 함수를 별도의 코루틴에서 실행하려면 일시 중단 함수 호출부를 별도의 코루틴 빌더로 감싸야 합니다.

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

### `일시 중단 함수의 호출 가능 지점`

- 코루틴 내부
- 일시 중단 함수

```kotlin
fun main() {
    runBlocking {
        delayAndPrint("부모 코루틴 실행 중")
        launch {
            delayAndPrint("자식 코루틴 실행 중")
        }
    }
}

suspend fun delayAndPrint(keyword: String) {
    delay(1000L)
    println("keyword: $keyword")
}
```

위처럼 코루틴에서는 일시 중단 함수를 호출할 수 있습니다.

<br>

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

### `일시 중단 함수에서 코루틴 실행하기`

- coroutineScope 함수를 사용하면 일시 중단 함수 내부에서 새로운 CoroutineScope 함수를 호출할 수 있다.

```kotlin
fun main() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val results = searchByKeyWord("Keyword")
        println("[결과], ${results.toList()}")
        println(getElapsedTime(startTime))
    }
}

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

fun getElapsedTime(startTime: Long): String = "소요 시간: ${System.currentTimeMillis() - startTime} ms"
```
```
[결과], [[DB] Keyword1, [DB] Keyword2, [Server] Keyword1, [Server] Keyword2]
소요 시간: 1033 ms
```

<br>

### `일시 중단 함수 예외 처리`

<img width="735" alt="스크린샷 2024-11-10 오후 3 52 14" src="https://github.com/user-attachments/assets/71513445-6afe-4717-9767-3217e7cabb59">

위의 코드를 보면 위처럼 구조화가 되어 있는 것을 볼 수 있습니다. 그러면 여기서 자식 코루틴에서 예외가 발생하면 어떻게 될까요?

<br>

<img width="844" alt="스크린샷 2024-11-10 오후 3 49 49" src="https://github.com/user-attachments/assets/46138c34-6c94-48ba-bf49-780620a44077">

위처럼 예외가 전파되어 부모 코루틴 및 다른 자식 코루틴까지 영향 받아 모두 실행되지 않는 것을 볼 수 있습니다.

이것을 방지하기 위해 `coroutineScope`이 아닌 `supervisorScope`을 사용할 수 있습니다.

<img width="828" alt="스크린샷 2024-11-10 오후 3 58 32" src="https://github.com/user-attachments/assets/348a48bd-e5a5-4912-945f-0d8dd52c97a4">

```kotlin
fun main() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val results = searchByKeyWord("Keyword")
        println("[결과], ${results.toList()}")
        println(getElapsedTime(startTime))
    }
}

suspend fun searchByKeyWord(keyword: String): Array<String> = supervisorScope {
    val dbResultsDeffered = async {
        throw Exception("DB 읽기 오류 발생")
        searchFromDb(keyword)
    }

    val serverResultsDeffered = async {
        searchFromServer(keyword)
    }

    // await에 대한 추가 예외 처리 try-catch 필요
    val dbResults = try {
        dbResultsDeffered.await()
    } catch (e: Exception) {
        arrayOf()
    }
    
    return@supervisorScope arrayOf(*dbResults, *serverResultsDeffered.await())
}

suspend fun searchFromDb(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[DB] ${keyword}1, [DB] ${keyword}2")
}

suspend fun searchFromServer(keyword: String): Array<String> {
    delay(1000L)
    return arrayOf("[Server] ${keyword}1, [Server] ${keyword}2")
}

fun getElapsedTime(startTime: Long): String = "소요 시간: ${System.currentTimeMillis() - startTime} ms"
```
```
[결과], [[Server] Keyword1, [Server] Keyword2]
소요 시간: 1035 ms
```

주의할 점은 Deffered는 await 호출할 때 예외를 추가로 노출하기 때문에 추가적인 try-catch 코드가 필요합니다.

