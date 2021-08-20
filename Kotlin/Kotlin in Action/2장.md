# `2장: 코틀린의 기초`

2장에서 다루는 내용

- 함수, 변수, 클래스, enum, 프로퍼티를 선언하는 방법

- 제어 구조

- 스마트 캐스트

- 예외 던지기와 예외 잡기

<br> <br>

## `2.1.2 함수`

```kotlin
fun max (a: Int, b: Int): Int {
    return if (a > b) a else b
}
```

- 함수를 선언할 때 `fun` 키워드를 사용합니다.

- 파라미터 이름 뒤에 그 파라미터 타입을 적습니다.

- 함수를 최상위 수준에 정의할 수 있습니다. 자바와 달리 꼭 클래스 안에 함수를 넣어야 할 필요가 없습니다.

- 배열도 일반적인 클래스와 마찬가지입니다. 코틀린에는 자바와 달라 배열 처리를 위한 문법이 따로 존재하지 않습니다.

<br>

```kotlin
fun max (a: Int, b: Int): Int = if (a > b) a else b
```

처음에 보았던 코드를 간략하게 줄이면 위와 같이 줄일 수 있습니다. 코틀린에서는 `식이 본문인 함수가 자주 사용됩니다.`

<br>

```kotlin
fun max (a: Int, b: Int) = if (a > b) a else b
```

위와 같이 반환 타입도 생략할 수 있습니다. 

> 생략 가능한 이유는 코틀린은 정적 타입 지정 언어이므로 컴파일 시점에 모든 식의 타입을 지정해야 했지만, 식이 본문인 함수의 경우 굳이 사용자가 반환 타입을 적지 않아도 컴파일러가 함수 본문 식을 분석해서 결과 타입을 함수 변환 타입으로 정해줍니다.

이렇게 컴파일러가 타입을 분석해 프로그래머 대신 프로그램 구성 요소의 타입을 정해주는 기능을 `타입 추론`이라 부릅니다. 식이 본문인 함수의 반환 티입만 생략 가능하다는 점을 알아야 합니다. 

<br> <br>

## `2.1.3 변수`

자바에서는 변수를 선언할 때 타입이 맨 앞에 오지만, 코틀린에서는 뒤에 선언합니다. 

```kotlin
val question = "String"
val answer = 42
```

그리고 위와 같이 `초기화할 값이 있다면 타입을 생략할 수 있습니다.`

<br>

### 변경 가능한 변수와 변경 불가능한 변수

- val(값을 뜻하는 value에서 따옴) - `변경 불가능한 참조를 저장하는 변수입니다.` 자바로 말하자면 final 변수로 해당합니다.

- var(변수를 뜻하는 variable에서 따옴) - `변경 가능한 참조입니다.` 자바의 일반 변수에 해당합니다.

<br> 

기본적으로는 모든 변수를 val 키워드를 사용해 불변 변수로 선언하고 나중에 꼭 필요할 때에만 var로 변경하는 것이 좋습니다. 

<br> <br>

## `2.1.4 더 쉽게 문자열 형식 지정: 문자열 템플릿`

```kotlin
fun main(args: Array<String>) {
    val name = if (args.size > 0) args[0] else "Kotlin"
    println("Hello, $name!");
}
```

이 문자열 템플릿은 자바의 문자열 접합 연산 `("Hello, " + name + "!"`과 동일한 기능을 하지만 좀 더 간결하다는 장점이 있습니다.

<br> <br>

## `2.1.4 문자열 템플릿`

```kotlin
fun main() {
    val name = "Gyunny"
    println("Hello $name")
}
```

문자열 템플릿을 사용해서 쉽게 변수를 출력할 수 있습니다. 

<br> <br>

## `2.2 클래스와 프로퍼티`

자바에서는 필드와 접근자를 한데 묶어 `프로퍼티(property)`라고 부릅니다. 코틀린 프로퍼티는 자바의 필드와 접근자 메소드를 완전히 대신합니다. `val로 선언한 프로퍼티는 읽기 전용이며, var로 선언한 프로퍼티는 변경 가능합니다.`

```kotlin
class Person (
   val name: String,       // 읽기 전용 (비공개 필드 + public getter)    
   var isMarried: Boolean  // 쓸 수 있는 프로퍼티 (비공개 필드 + public getter, setter)
)

fun main() {
    val person = Person("Gyunny", true) // 객체 생성할 때 new를 사용하지 않음
    println(person.name) // getter 처럼 사용 가능
}
```

<br> <br>

## `2.3 선택 표현과 처리: enum과 when`

when은 자바의 switch를 대치하되 훨씬 더 강력하며, 앞으로 더 자주 사용할 프로그래밍 요소라고 생각할 수 있습니다. 

<br>

### enum 클래스 정의

```kotlin
enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}
```

위와 같이 단순하게 작성할 수도 있지만, enum 클래스 안에도 프로퍼티나 메소드를 정의할 수 있습니다. 

<br>

```kotlin
enum class Color(val r: Int, val g: Int, val b: Int) {
    RED(255, 0, 0), ORANGE(255, 165, 0), YELLOW(255, 255, 0);  // 코틀린에서 유일하게 세미콜론이 필요한 부분
    
    fun rgb() = (r * 256 + g) * 256 + b
}
```

<br> <br>

### `when으로 enum 클래스 다루기`

```kotlin
fun getMnemonic(color: Color) {
    when (color) {
        Color.RED -> "A"
        Color.ORANGE -> "B"
        Color.YELLO -> "C"
    }
}
```

자바와 달리 각 분기의 끝에 `break`를 넣지 않아도 됩니다.

```kotlin
fun getMnemonic(color: Color) {
    when (color) {
        Color.RED, Color.ORANGE -> "B"
        Color.YELLO -> "C"
    }
}
```

그리고 위와 같이 `,`를 사용해서 하나로 묶을 수도 있습니다. 

```kotlin
fun getMnemonic(color: Color) {
    when (color) {
        RED, ORANGE -> "B"
        YELLO -> "C"
    }
}
```

또한 위처럼 코드를 더욱 줄일 수도 있습니다.

<br> <br>

## `when과 임의의 객체를 함께 사용`

자바에서는 분기 조건에 상수(enum), 숫자만을 사용할 수 있는데 반해 `코틀린 when의 분기 조건은 임의의 객체를 허용`합니다. 

```kotlin
fun mix(c1: Color, c2: Color) =
    when (setOf(c1, c2)) {
        setOf(Color.RED, Color.ORANGE) -> Color.ORANGE
        setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
        else -> throw Exception("Dirty color")
    }

enum class Color(
    var r: Int, var g: Int, val b: Int
) {
    RED(255, 0, 0,), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), BLUE(0, 0 ,255),
    GREEN(255, 165, 0)
}
```

`setOf` 인자로 전달받은 여러 객체를 포함하는 집합인 Set 객체로 만드는 함수입니다.  

<br> <br>

## `인자 없는 when 사용`

위의 예제는 분기마다 Set 인스턴스를 생성해서 비효율적이기도 한데요. 그래서 아래와 같이 인자를 사용하지 않고도 사용하는 방법도 존재합니다. 

```kotlin
fun mixOptimized(c1: Color, c2: Color) =
    when {
        (c1 == RED && c2 == YELLOW) ||
            (c2 == YELLOW && c2 == RED) ->
            ORANGE
        else -> throw Exception("Dirty color")
    }

enum class Color(
    var r: Int, var g: Int, val b: Int
) {
    RED(255, 0, 0,), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), BLUE(0, 0 ,255),
    GREEN(255, 165, 0)
}
```

위와 같이 `when에 인자가 없으려면 각 분기의 조건이 불리언 결과를 계산하는 식이어야` 합니다. 

<br> <br>

## `2.3.5 스마트 캐스트: 타입 검사와 타입 캐스트를 조합`

```java
public class Car {
    String color;
    int door;
    
    void drive() {
        System.out.println("드라이브");
    }
    
    void stop() {
        System.out.println("멈춰");
    }
}

class FireEngine extends Car {
    void water() {
        System.out.println("물뿌리기");
    }
}

class Ambulance extends Car {
    void siren() {
        System.out.println("싸이렌");
    }
}
```
```java
public class Test {
    void doWork(Car c) {
        if (c instanceof FireEngine) {
            FireEngine fe = (FireEngine)c;
            fe.siren();
        }
        else if (c instanceof Ambulance) {
            Ambulance a = (Ambulance)c;
            a.siren();
        }
    }    
}
```

코틀린의 `스마트 캐스트`를 보기 전에 자바의 `instanceof`를 먼저 보고 가겠습니다. 자바에서 instanceof를 사용하면 Test 클래스에서 볼 수 있듯이 `다운캐스팅`을 사용해야 그 해당 타입의 변수 또는 메소드에 접근할 수 있습니다. 
매번 이렇게 형변환을 해줘야 하면 번거롭기도 하고 에러가 발생할 수도 있는 확률이 존재하는데요. 

이러한 자바 instanceof의 단점을 `코틀린 스마트 캐스트`로 편하게 대체할 수 있습니다. 

```kotlin
interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr
```

```kotlin
fun eval(e: Expr) : Int {
    if (e is Num) {
        val n = e as Num // 불필요한 중복
        return n.value
    }
    
    if (e is Sum) {
        return eval(e.right) + eval(e.left)
    }
    
    throw IllegalArgumentException("Unknown expression")
}
```

코틀린에서 `is`는 자바의 `instanof`와 비슷한데요. 코틀린에서는 자바와 달리 따로 형변환을 해주지 않아도 `컴파일러가 대신 캐스팅을 해줍니다.` 이것을 `스마트 캐스트`라고 부릅니다.

> 클래스의 프로퍼티에 대해 스마트 캐스트를 사용한다면 그 프로퍼티는 반드시 val이어야 하며 커스텀 접근자를 사용한 것이어도 안된다. 
> 원하는 타입으로 명시적으로 타입 캐스팅하려면 as 키워드를 사용한다. 

<br> <br>

## `리팩터링: if를 when으로 변경`

```kotlin
if (a > b) a else b // Kotlin
a > b ? a : b       // Java
``` 

위의 코틀린 코드는 자바에서 삼항 연산자와 동일하게 존재합니다. 그래서 코틀린에서는 삼항 연산자가 존재하지 않습니다. 

```kotlin
fun eval (e: Expr) : Int = 
    if (e is Num) {
        e.value
    } else if (e is Sum) {
        eval(e.right) + eval(e.left)
    } else {
        throw IllegalArgumentException("Unknown expression")
    }
```

그래서 위에서 보았던 eval 함수를 리팩터링 하면 이렇게 return 문도 생략하고 할 수 있습니다. 

```kotlin
fun eval(e: Expr): Int =
    when (e) {
        is Num ->
            e.value
        is Sum ->
            eval(e.right) + eval(e.left)
        else ->
            throw IllegalArgumentException("Unknown expression")
    }
```

when을 사용해도 if 문을 사용했을 때와 마찬가지로 타입을 검사하고 나면 자동으로 `스마트 캐스트`가 이루어집니다. 

<br> <br>

## `코틀린 for 루프`

```kotlin
for (x in 1..5) { // 1~5 출력
    print(x) // "12345"
}

for (x in 5..1) { 
    print(x) // 아무것도 출력되지 않음
}

for (x in 5 downTo 1) {
    print(x)  // "54321" 역순으로 출력
}

for (x in 1..5 step 2) {
    print(x)  // "135"
}

for (x in 5 downTo 1 step 2) {
    print(x)  // "531"
}
```

for문은 자바의 for-each와 비슷합니다. 나머지 형태도 참고해서 보면 어떻게 사용해야 하는지 금방 알 수 있습니다. 

<br> <br> 

## `Map에 대한 이터레이션`

```kotlin
fun main(args: Array<String>) {
    runApplication<AusgApplication>(*args)

    val binaryReps = TreeMap<Char, String>()

    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.code)
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

}
```
```
A = 1000001
B = 1000010
C = 1000011
D = 1000100
E = 1000101
F = 1000110
```

위와 같이 `Map` 형태로 반복문으로 출력할 수 있습니다. 

<br> <br>

## `in으로 컬렉션이나 범위의 원소 검사`

```kotlin
fun main() {

    println(isLetter('q'))     // ture
    println(isNotDigit('x'))   // true
}

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'..'9'
```

..과 in을 같이 사용하면 강력하게 사용할 수 있는 거 같습니다. 

<br>

```kotlin
fun recognize(c: Char) = when (c) {
    in '0'..'9' -> 'It's a digit!'
    in 'a'..'z', in 'A'..'Z' -> 'It's a letter!' 
}
```

그리고 위와 같이 `when`, `in, ..`을 같이 사용하면 훨씬 더 코틀린의 편리함을 느낄 수 있습니다. 

<br> <br>

## `코틀린의 예외 처리`

```kotlin
if (percentage !in 0..100) {
    throw IllegalArgumentException(
        "퍼센트는 0에서 100 사이만 가능!"
    )
}

val percentage = 
    if (number in 0..100)
        number
    else
        throw IllegalArgumentException(
            "퍼센트는 0에서 100 사이만 가능!"
        )
```

위와 같이 코틀린에서도 자바랑 크게 다르지 않게? 작성할 수 있습니다.

<br>

## `try, catch, finally`

```kotlin
fun readNumber(reader: BufferedReader): Int? {
    try {
        val line = reader.readLine()
        return Integer.parseInt(line)
    }
    catch (e: NumberFormatException) {
        return null
    } finally {
        reader.close()
    }
}
```

자바 코드와 가장 큰 차이점은 `throws 절이 코드에 없다는 것`입니다. 자바에서는 함수를 작성할 때 함수 선언 뒤에 `throws IOExeption`을 넣어야 합니다. 왜냐하면 `IOException`은 `체크 예외`이기 때문에 자바에서는 체크 예외를 명시적으로 처리해야 하기 때문입니다.
하지만 코틀린에서는 `체크 예외`, `언체크 예외`를 구분하지 않습니다. 

<br> <br>

## `try를 식으로 이용`

finally 절을 없애는 코드를 보면 아래와 같습니다.

```kotlin
fun readNumber (reader: BufferedReader) {
    val number = try {
        Integer.parseInt(reader.readLine())
    } catch (e: NumberFormatException) {
        return
    }
    
    println(number)
}
```

코틀린의 `try 키워드`는 if나 when나 마찬가지로 `식`입니다. `하나 차이가 있다면 if와 달리 try의 본문을 반드시 중괄호 { }로 둘러싸야 합니다.`
