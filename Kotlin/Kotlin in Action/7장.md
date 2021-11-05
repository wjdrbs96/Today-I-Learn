# `7장: 연산자 오버로딩과 기타 관례`

어떤 클래스 안에 plus 라는 이름의 특별한 메소드를 정의하면 그 클래스의 인스턴스에 대해 + 연산자를 사용할 수 있습니다. 이런식으로 `어떤 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법을 코틀린에서는 관례`라고 부릅니다. 

<br> <br>

## `산술 연산자 오버로딩`

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
```

```
val p1 = Point(10, 30)
val p2 = Point(30, 40)
println(p1 + p2)   // +로 계산하면 "plus" 함수가 호출된다.
```

plus 함수 앞에 `operator` 키워드를 붙여야 합니다. 연산자를 오버로딩하는 함수 앞에는 꼭 operator가 있어야 합니다. 

<br>

```kotlin
operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
```

그리고 연산자를 멤버 함수로 만드는 대신 `확장 함수`로 정의할 수도 있습니다.

<br>

### `오버로딩 가능한 이항 산술 연산자`

|식|함수 이름|
|-------|--------|
| a * b | times |
| a / b | div |
| a % b | mod(1.1부터 rem) |
| a + b | plus |
| a - b | minus |

<br> <br>

## `두 피연산자의 타입이 다른 연산자 정의하기`

```kotlin
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}
```
```
val p = Point(10, 30)
println(p * 1.5)
Point(x=15, y=30)
```

연산자를 정의할 때 두 피연산자가 다른 타입일 필요는 없습니다. 그리고 코틀린 연산자가 자동으로 `교환 법칙(a op b == b op a 성질)`을 지원하지느 않음을 유의해야 합니다.

`p * 15` 외에 `15 * p` 라고도 쓸 수 있어야 한다면 `p * 15`와 같은 식에 대응하는 아래와 같은 연산자 함수를 정의해야 합니다.  

```kotlin
operator fun Double.times(p: Point): Point
```

<br> <br>

## `결과 타입이 피연산자 타입과 다른 연산자 정의하기`

```kotlin
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}
```
```
println('a' + 3)
aaa
```

이 연산자는 Char을 좌항으로 받고 Int를 우항으로 받아서 String을 돌려줍니다. 이런 식의 피연산자와 결과 타입 조합도 `연산자 오버로딩`이라고 할 수 있습니다. 

<br> <br>

## `복합 대입 연산자 오버로딩`

```kotlin
val point = Point(1, 2)
point += Point(3, 4)
println(point)
Point(x=4, y=6)
```

plus와 같은 연산자 오버로딩도 지원하지만, `+=, -= 등의 연산자와 같은 복합 대입 연산자`도 지원합니다. 그리고 경우에 따라 += 연산이 객체 대한 참조를 다른 참조로 바꾸기보다 원래 객체의 내부 상태를 변경하게 만들고 싶을 때가 있습니다 

```kotlin
val numbers = ArrayList<Int>()
numbers += 42
println(numbers[0]) // 42
```

<br> <br>

## `단항 연산자 오버로딩`

```kotlin
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

val p = Point(10, 20)
println(-p) // Point(x=-10, y=-10)
```

단항 연산자를 오버로딩하는 것도 이항 연산자와 같습니다. 미리 정해진 이름의 함수를 선언하면서 `operator`로 표시하면 됩니다.

<br> <br>

## `동등성 연산자: equals`

`코틀린은 == 연산자 호출을 equals 메소드 호출로 컴파일`합니다. `!=` 연산자를 사용하는 식도 마찬가지로 `equals`로 컴파일 됩니다. a == b 라는 비교를 처리할 때 코틀린은 a가 널인지 판단해서 널이 아닌 경우에만 `a.equals(b)`를 호출합니다. a가 널이라면 b도 널인 경우에만 결과가 true 입니다.

```kotlin
class Point(val x: Int, val y: Int) {
    override fun equals(obj: Any?): Boolean {
        if (obj === this) return true
        if (obj !is Point) return false
        return obj.x == x && obj.y == y
    }
}
```

`식별자 비교 연산자(===)`는 자바의 `==` 연산자와 같습니다. 따라서 `====`는 자신의 두 피연사자가 서로 같은 객체를 가르키는지 비교합니다. (참고로 `===`는 오버로딩 할 수 없습니다.)

<br> <br>

## `순서 연산자: compareTo`

자바에서는 값을 비교해야 할 때 `Comparable` 인터페이스의 `compareTo` 메소드를 구현해서 사용했습니다. 코틀린에서도 똑같은 `Comparable` 인터페이스를 지원하는데, `비교 연산자 (<, >, <=, >=)`는 `compareTo` 호출로 컴파일됩니다. 

```kotlin
class Person(
    val firstName: String,
    val lastName: String
): Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}
```
```
val p1 = Person("Alice", "Smith")
val p2 = Person("Bob", "Johson")
println(p1 < p2) // false
```

위의 코드는 코틀린 표준 라이브러이의 `compareValueBy` 함수를 사용해 `compareTo`를 쉽고 간결하게 정의할 수 있음을 보여줍니다. 

<br> <br>

## `인덱스로 원소에 접근: get과 set`

인덱스 연산자를 사용해 원소를 읽는 연산은 `get 연산자 메소드로 반환되고`, 원소를 쓰는 연산은 `set 메소드 연산자 메소드`로 반환됩니다. 

```kotlin
operator fun Point.get(index: Int): Int {
    return when(index) {
        0 -> x
        1 -> y
        else ->
            throw kotlin.IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```

get이라는 메소드를 만들고 `operator` 변경자를 붙이기만 하면 `p[1] 이라는 식은 위의 정의한 get 메소드로 변환됩니다.`

<br>

```kotlin
data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else ->
            throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```
```
val p = MutablePoint(10, 20)
p[1] = 42
println(p)  // MutablePoint(x=10, y=42)
```

위의 코드도 `p[1] = 42`를 통해서 위에서 정의한 `set 메소드`가 호출됩니다. 

<br> <br>

## `in 관례`

컬렉션이 지원하는 다른 연산자로는 `in`이 있습니다. `in` 연산자와 대응하는 함수는 `contains` 입니다. `in 연산자는 contains 함수 호출로 변환됩니다.`

<br> <br>

## `rangeTo 관례`

범위를 만들려면 `.. 구문`을 사용해야 합니다. 예를 들어 1..10은 1부터 10까지 모든 수가 들어있는 범위를 가르킵니다. `start..end는 start.rangeTo(end)` 함수 호출로 컴파일 됩니다. range 함수는 Comparable에 대한 `확장 함수`입니다. 

LocalDate 클래스를 사용해 날짜의 범위를 만들어보겠습니다. 

```kotlin
val now = LocalDate.now()
val vacation = now..now.plusDays(10)
println(now.plusWeeks(1) in vacation) // true
```

특정 날짜가 날짜 범위 안에 들어가는지 검사하는 코드입니다. 위의 코드를 보면 `..`과 `in`이 사용된 것을 볼 수 있습니다. 

<br> <br>

## `for 루프를 위한 iterator 관례`

```kotlin
operator fun CharSequence.iterator(): CharIterator
```
```
>>> for (c in "abc")
```

코틀린에서는 `iterator` 메소드를 확장 함수로 정의할 수 있습니다. 이런 성질로 인해 자바 문자열에 대한 for 루프가 가능합니다.

<br> <br>

## `구조 분해 선언과 component 함수`

```kotlin
val p = Point(10, 20)
val (x, y) = p
println(x) // 10
println(y) // 20 
```

내부에서 구조 분해 선언은 다시 관례를 사용합니다. 구조 분해 선언의 각 변수를 초기화하기 위해 `componentN` 이라는 함수를 호출합니다.

<br> <br>

## `구조 분해 선언과 루프`

```kotlin
fun printEntries(map: Map<String, String>) {
    for ((key, value) in map) {
        println("$key -> $value")
    }
}
```
```
val map = mapOf("Oracle", to "Java", "JetBrains" to "Kotlin")
printEntries(map)
Oracle -> Java
JetBrains -> Kotlin
```

Map 원소에 대해 이터레이션할 때 구조 분해 선언이 유용하게 사용됩니다. 코틀린 표준 라이브러리에는 맵에 대한 확장 함수로 `iterator`가 들어있습니다. 또한 코틀린 라이브러리는 `Map.Entity`에 대한 확장 함수로 component1과 component2를 제공합니다. 

```kotlin
for (entry in map.entries) {
    val key = entry.component1()
    val value = entry.component2()
}
```

<br> <br>

## `프로퍼티 접근자 로직 재활용: 위임 프로퍼티`

코틀린이 제공하는 관례에 의존하는 툭성 중에 독특하면서 강력한 기능인 `위임 프로퍼티`가 있습니다. 위임 프로퍼티를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현할 수 있습니다. 또한 그 과정에서 접근자 로직을 매번 재구현할 필요도 없습니다. 

예를 들어 프로퍼티는 위임을 사용해 자신의 값을 필드가 아니라 데이터베이스 테이블이나 브라우저, 세션, 맵 등에 저장할 수 있습니다. 

<br> <br>

## `위임 프로퍼티 소개`

```kotlin
class Foo {
    val p: type by Delegate()
}
```

p 프로퍼티는 접근자 로직을 다른 객체에게 위임합니다. 여기서는 `Delegate` 클래스의 인스턴스를 위임 객체로 사용합니다. 

<br>

```kotlin
class Foo {
    private val delegate: Delegate()  // 컴파일러가 생성한 도우미 프로퍼티
    val p: Type
    set(value: Type) = delegate.setValue(..., value)  // p 프로퍼티를 위해 컴파일러가 생성한 접근자는 "delegate" get, set 메소드 호출
    get() = delegate.getValue(...)    
}
```
```
val foo = Foo()
val oldValue = foo.p // foo.p 라는 프로퍼티 호출은 내부에서 delegate.getValue() 호출
foo.p = new Value    // foo.p = new Value => 내부에서 delegate.setValue() 호출
```

프로퍼티 위임 관례를 따르는 `Delegate` 클래스는 `getValue`, `setValue` 메소드를 제공해야 합니다. 그리고 코틀린 라이브러리는 프로퍼티 위임을 사용해 프로퍼티 초기화를 지연시켜주는 기능이 있는데, 이것이 `위임 프로퍼티`의 강력함을 보여주는 한 가지 예입니다. 

<br> <br>

## `위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연`

지연 초기화는 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분이 필요한 경우 초기화할 때 흔히 쓰이는 패턴입니다. 초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 꼭 초기화하지 않아도 되는 프로퍼티에 대해 `지연 초기화 패턴`을 사용할 수 있습니다.

```kotlin
fun loadEmails(person: Person): List<Email>  {
    println("${person.name}의 이메일을 가져옴")
    return listof(...)
}
```

Person 클래스가 자신이 작성한 이메일의 목록을 제공한다고 가정하겠습니다. 이메일은 데이터베이스에 있어, 불러오기에는 시간이 오래 걸려 이메일 프로퍼티의 값을 최초로 사용할 때 `단 한번만` 데이터베이스에서 가져오고 싶다고 가정하겠습니다. 

<br>

```kotlin
class Person(val name: String) {
    private val _emails: List<Email>? = null  // 데이터를 저장하고 emails의 위임
    val emails = List<Email>
        get() {
            if (_emails == null) {
                _emails = loadEmails(this)
            }
            return _emails!!
        }
}
```

이메일을 불러오기 전에는 null을 저장하고, 불러온 다음에는 이메일 리스트를 저장하는 `emails` 프로퍼티를 추가해서 `지연 초기화`를 구현한 클래스를 보여줍니다. 여기서는 `뒷밤침하는 프로퍼티`라는 기법을 사용합니다. 다른 프로퍼티인 `emails`는 `_emails`라는 프로퍼티에 대한 읽기 연산을 제공합니다. 

하지만 `지연 초기화해야 하는 프로퍼티 코드가 많아지면 어떻게 될까요?` 이 구현은 스레드 안전하지 않아서 언제나 제대로 작동한다고 말할 수도 없습니다. 물론 코틀린은 더 나은 해법을 제공합니다. `위임 프로퍼티를 사용하면 됩니다.` 위임 프로퍼티는 데이터를 저장할 때 쓰이는 뒷받침하는 프로퍼티와 값이 오직 한 번만 초기화됨을 보장하는 게더 로직을 함께 캡슐화해줍니다. 

```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```

lazy 함수는 코틀린 관례에 맞는 시그니처의 getValue 메소드가 들어있는 객체를 반환합니다. 따라서 `by 키워드와 함께 사용해 위임 프로퍼티를 만들 수 있습니다.` lazy 함수는 스레드에 안전합니다. 하지만 필요에 따라 동기화에 사용할 락을 lazy 함수에 전달할 수도 있고, 다중 스레드 환경에서 사용하지 않을 프로퍼티를 위해 lazy 함수가 동기화를 하지 못하게 막을 수도 있습니다.  

<br> <br>

## `위임 프로퍼티 컴파일 규칙`

```kotlin
class C {
    var prop: Type by MyDelegate()
}
```

컴파일러는 MyDelegate 클래스의 인스턴스를 감춰진 프로퍼티에 저장하며 그 감춰진 프로퍼티를 `<delegate>` 라고 부릅니다.

<br> <br>

## `프로퍼티 값을 맵에 저장`

자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 `위임 프로퍼티`를 활용하는 경우가 자주 있습니다. 그런 객체를 `확장 가능한 객체`라고 부르기도 합니다. 

```kotlin
class Person {
    // 추가 정보
    private val _attributes = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes [attrName] = value
    }
    
    // 필수 정보
    val name: String
    get() = _attributes["name"]!!
}
```

위의 코드에서 쉽게 위임 프로퍼티를 활용하게 변경할 수 있습니다. `by 키워드 뒤에 맵을 직접 넣으면 됩니다.`

<br>

```kotlin
class Person {
    private val _attribute = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }
    
    val name: String by _attribute
}
```

표준 라이브러리가 Map과 MutableMap 인터페이스에 대해 getValue와 setValue 확장 함수를 제공하기 때문입니다. 