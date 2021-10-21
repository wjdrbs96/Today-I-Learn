# `6장: 코틀린 타입 시스템`

## `널 가능성`

널 가능성은 `NullPointerException` 오류를 피할 수 있게 돕기 위한 코틀린 타입 시스템의 특성입니다. 코틀린을 비롯한 최신 언어에서 null에 대한 접근 방법은 가능한 이 문제를 `실행 시점에서 컴파일 시점`으로 옮기는 것입니다. 

<br> <br>

## `널이 될 수 있는 타입`

널이 될 수 있는 타입은 프로그램 안의 프로퍼티나 변수에 null을 허용하게 만드는 방법입니다. null이 될 수 있는 그 변수에 대해 메소드를 호출하면 `NullPointerException`이 발생할 수 있으므로 안전하지 않습니다. 코틀린은 그런 메소드 호출을 금지함으로써 많은 오류를 방지합니다. 

```java
int strLen(String s) {
    return s.length();
}
```

위의 자바 코드에서 매개변수로 null이 들어오면 `NullPointerException`이 발생합니다. 자바 코드를 코틀린 코드로 바꿔보겠습니다. 

<br>

```kotlin
fun strLen(s: String) = s.length
```

코틀린은 기본적으로 null을 허용하지 않기 때문에 매개변수로 null을 넘기지 못합니다. 즉, `NullPointerException`이 발생하지 않는다고 장담할 수 있습니다. 

<br>

```kotlin
fun strLen(s: String?) = ...
```

인자로 null을 받기 위해서는 타입 이름 뒤에 `?`를 사용하면 됩니다. 어떤 타입이든 타입 이름 뒤에 물음표를 붙이면 그 타입의 변수나 프로퍼티에 null 참조를 저장할 수 있다는 뜻입니다. 널이 될 수 있는 타입인 변수에 대해 `변수.메소드()` 처럼 메소드를 직접 호출할 수는 없습니다. 

```kotlin
fun strLenSafe(s: String?) = s.length()  // 불가능
```

널이 될 수 있는 값을 널이 될 수 없는 타입의 변수에 대입하는 것도 불가능합니다.

```kotlin
val x: String? = null
var y: String = x    // 불가능
```

널이 될 수 있는 타입의 값은 상당히 제약히 많아 보여 불편해보이지만, 가장 중요한 일은 `null과 비교하는 일입니다.`

```kotlin
fun strLenSafe(s: String): Int =
    if (s != null) s.length else 0

val x: String? = null
println(strLenSafe(x))  // null 검사를 추가하면 코드가 컴파일 됨
```

<br> <br>

## `안전한 호출 연산자: ?.`

코틀린이 제공하는 가장 유용한 도구 중 하나가 안전한 호출 연산자인 `?.` 입니다. `?.`은 null 검사와 메소드 호출을 한 번의 연산으로 수행합니다. 

```kotlin
s?.toUpperCase()  // 호출하려는 값이 null이면 호출이 무시되고 null이 결과 값이 된다.
```

```kotlin
if (s != null) s.toUpperCase() else null
```

위의 두 코드는 같습니다. 그리고 `안전한 호출의 결과 타입도 널이 될 수 있는 타입이라는 사실을 유의해야 합니다.`

<br> <br>

## `엘비스 연산자: ?:`

코틀린은 null 대신 사용할 디폴트 값을 지정할 때 편리하게 사용할 수 있는 연산자를 제공합니다. 그 연산자는 `엘비스 연산자` 라고 합니다. 

```kotlin
fun foo(s: String?) {
    val t: String = s ?: "" // "s"가 null 이면 결과는 빈 문자열 ""
}
```

이 연산자는 이항 연산자로 좌항을 계산한 값이 널인지 검사합니다. 좌항 값이 널이 아니면 좌항 값을 결과로 하고, 좌항 값이 널이면 우항 값을 결과로 합니다. 

엘비스 연산자를 객체가 널인 경우 널을 반환하는 안전한 호출 연산자와 함께 사용해서 객체가 널인 경우에 대비한 값을 지정하는 경우도 많습니다. 

```kotlin
fun strLenSafe(s: String?): Int = s?.length ?: 0
```

그리고 코틀린에서는 return이나 throw 등의 연산도 식입니다. 따라서 엘비스 연산자의 우항에 return throw 등의 연산을 넣을 수 있습니다.

```kotlin
val address = person.company?.adrress ?: throw IllegalArgumentException()
```

<br> <br>

## `안전한 캐스트: as?`

자바 타입 캐스트와 마찬가지로 대상 값을 as로 지정한 타입으로 바꿀 수 없으면 `ClassCaseException`이 발생합니다. 물론 as를 사용할 때마다 is를 통해 미리 as로 변환 가능한 타입인지 검사해볼 수 있습니다. 하지만 안전하면서 간결한 언어를 지향하는 코틀린에서는 좀 더 나은 해법이 존재합니다.

> as? 연산자는 어떤 값을 지정한 타입으로 캐스트합니다. as?는 값을 대상 타입으로 변환할 수 없으면 null을 반환합니다.

<br>

```kotlin
val otherPerson = o as? Person ?: return false
```

원하는 타입인지 쉽게 검사하고 캐스트할 수 있고, 타입이 맞지 않으면 쉽게 false를 반환하는 코드를 작성할 수 있습니다. 

<br> <br>

## `널 아님 단언: !!`

`널 아님 단언`은 코틀린에서 널이 될 수 있는 타입의 값을 다룰 때 사용할 수 있는 도구 중에서 가장 단순한 도구입니다. 느낌표를 `!!` 이중으로 사용하면 어떤 값이든 널이 될 수 없는 타입으로 강제로 바꿀 수 있습니다. 실제 널에 대해 !!를 적용하면 NPE가 발생합니다. 

```kotlin
fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(sNotNull.length)
}
```

위의 코드에서 s에 null이 들어가면 NPE가 발생한다는 것은 쉽게 예측할 수 있습니다. 하지만 발생한 예외는 `sNotNull.length`가 아니라 `s!!` 단언문이 위치한 곳을 가리킨다는 것을 알아야 합니다. 

<br> <br>

## `알아두기`

!!를 널에 대해 사용해서 발생하는 예외의 스택 트레이스에는 어떤 파일의 몇 번째 줄인지에 대한 정보는 들어있지만 어떤 식에서 예외가 발생했는지에 대한 정보는 들어있지 않습니다. 즉, `어떤 값이 널이었는지 확실히 하기 위해 여러 !! 단언문을 한 줄에 함께 쓰는 일을 피해야 합니다.`

```kotlin
person.company!!.address!!.country // 이런식으로 코드를 작성하지 말기
```

널이 될 수 있는 타입 값을 널이 아닌 값만 인자로 받는 함수에 넘기려면 어떻게 하면 좋을까요? 그런 호출은 안전하지 않기 때문에 컴파일러에서 그 호출을 허용하지 않습니다. 이 때 사용할 수 있는 함수가 `let` 입니다.

<br> <br>

## `let 함수`

let 함수를 사용하면 널이 될 수 있는 식을 더 쉽게 다룰 수 있습니다. `let 함수는 안전한 호출 연산자와 함께 사용하면 원하는 식을 평가해서 결과가 널인지 검사한 다음에 그 결과를 변수에 넣는 작업을 간단한 식을 사용해 한꺼번에 처리할 수 있습니다.`

위에서 말했듯이, `let을 사용하는 가장 흔한 용례는 널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘기는 경우입니다.` 

```kotlin
fun sendEmailTo(email: String) {}
```

이 함수에게는 null을 넘길 수 없습니다. null을 인자로 받기 위해서는 `if 문으로 null 검사가 필요합니다.` 하지만 let 함수를 통해 인자를 전달하는 방법에 대해서 알아보겠습니다. 

널이 될 수 있는 값에 대해 안전한 호출 구문을 사용해 let을 호출하되 널이 될 수 없는 타입을 인자로 받는 람다를 let에 전달합니다. 이렇게 하면 널이 될 수 있는 타입의 값을 널이 될 수 없는 타입의 값으로 바꿔서 람다에 전달하게 됩니다. 즉, `email이 null이 아닌 경우에만 let 함수를 통해서 람다가 실행됩니다.`

```kotlin
email?.let { email -> sendEmailTo(email) }
email?.let { sendEmailTo(it) }  // it을 사용해서 더 짧게도 가능
```

<br> <br>

## `나중에 초기화할 프로퍼티`

코틀린에서 클래스 안의 널이 될 수 없는 프로퍼티를 생성자 안에서 초기화하지 않고 특별한 메소드 안에서 초기화할 수는 없습니다. 코틀린에서는 일반적으로 생성자에서 모든 프로퍼티를 초기화해야 합니다. 

이 때 사용할 수 있는 것이 `지연 초기화`가 있습니다. `lateinit` 변경자를 붙이면 프로퍼티를 나중에 초괴화 할 수 있습니다. 

```kotlin
class MyTest {
    private lateinit var myService: MyService  // 초기화 하지 않고 널이 될 수 없는 프로퍼티를 선언
}
```

나중에 초기화 하려는 프로퍼티는 항상 `var` 이어야 합니다. val 프로퍼티는 final 필드로 컴파일되며, 생성자 안에서 반드시 초기화 되어야 합니다. 

<br> <br>

## `6.1.9 널이 될 수 있는 타입 확장`

널이 될 수 있는 타입에 대한 확장 함수를 정의하면 null 값을 다루는 강력한 도구로 활용할 수 있습니다. 어떤 메소드를 호출하기 전에 수신 객체 역할을 하는 변수가 널이 될 수 없다고 보장하는 대신, 직접 변수에 대해 메소드를 호출해도 확장 함수인 메소드가 알아서 널을 처리해줍니다.

예를들어 코틀린 라이브러리에서 `String을 확장해 정의된 isEmpty, isBlank` 함수들은 빈 문자열, 공백을 검사하기 위해서 사용할 것인데요. 이러한 함수처럼 `null`을 미리 검사할 수 있으면 상당히 편리할 것 같은데요. 실제로 `String? 타입의 수신 객체에 대해 호출할 수 있는 isNullOrEmpty, isNullOrBlank` 메소드가 있습니다. 

```kotlin
fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) {    // 안전한 호출을 하지 않아도 됨
        println("Please fill in the required fileds")
    }
}
```

```
verifyUserInput(" ")
> Please fill in the required fileds

verifyUserInput(null)
> Please fill in the required fileds
```

안전한 호출 없이도 널이 될 수 있는 수신 객체 타입에 대해 선언된 확장 함수를 호출 가능합니다.

<br> <br>

## `타입 파라미터와 널 가능성`

코틀린에서는 함수나 클래스의 모든 타입 파라미터는 기본적으로 null이 될 수 있습니다. 따라서 `타입 파라미터 T를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 물음표가 없더라도 T가 널이 될 수 있는 타입입니다.`

```kotlin
fun <T> printHashCode(t: T) {
    println(t?.hashCode())  // t가 null 일 수 있으므로 안전한 호출을 사용해야 함
}

printHashCode(null) // T 타입은 Any?로 추론 됨
```

t 파라미터의 타입 이름 T에는 물음표가 붙어 있지 않지만 t는 null을 받을 수 있습니다. 타입 파라미터가 널이 아님을 확실히 하려면 널이 될 수 없는 `타입 상항`을 지정해야 합니다. 

<br>

```kotlin
fun <T: Any> printHashCode(t: T) {
    println(t?.hashCode())
}
```

타입 파라미터는 널이 될 수 있는 타입을 표시하려면 반드시 물음표를 타입 이름 뒤에 붙어야 한다는 규칙의 유일한 예외입니다. 

<br> <br>

## `널 가능성과 자바`

코틀린은 자바 상호운용성을 강조하는 언어입니다. 하지만 자바는 널 가능성을 지원하지 않는데요. 이러한 차이를 어떻게 해결하고 있는지 알아보겠습니다. 

<br>

### `플랫폼 타입`

`플랫폼 타입은 코틀린이 널 관련 정보를 알 수 없는 타입을 말합니다.` 그 타입은 널이 될 수 있는 타입으로 처리해도 되고 널이 될 수 없는 타입으로 처리해도 됩니다. 이는 자바와 마찬가지로 플랫폼 타입에 대해 수행하는 모든 연산에 대한 책임은 우리에게 있다는 뜻입니다.

코틀린은 보통 널이 될 수 없는 타입의 값에 대해 널 안전성을 검사하는 연산을 수행하면 경고를 표시하지만 플랫폼 타입의 값에 대해 널 안전성 검사를 중복 수행해도 아무 경고도 표시하지 않습니다.

```java
public class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

위의 자바 코드에서 `getName()`은 `null`을 리턴할지 아닐지 알 수 없습니다. 즉, 널 체크를 우리가 직접 해주어야 합니다. 

<br> <br>

### `상속`

코틀린에서 자바 메소드를 오바라이드할 때 그 메소드의 파라미터와 반환 타입을 널이 될 수 있는 타입으로 선언할 지 널이 될 수 없는 타입으로 선언할 지 결정해야 합니다. 

```java
interface StringProcessor {
    void process(String value);
}
```
```kotlin
class StringPrinter : StringProcessor {
    override fun process(value: String) {
        println(value)
    }
}

class NullableStringPrinter : StringProcessor {
    override fun process(value: String?) {
        if (value != null) {
            println(value)
        }
    }
}
```

자바 클래스나 인터페이스를 코틀린에서 구현할 경우 널 가능성을 제대로 처리하는 일이 중요합니다. 널이 될 수 없는 타입으로 선언한 모든 파라미터에 대해 널이 아님을 검사하는 단언문을 만들어줍니다.

<br> <br>

## `원시 타입: Int, Boolean`

코틀린은 원시 타입과 래퍼 타입을 구분하지 않으므로 항상 같은 타입을 사용합니다. 

```kotlin
val i: Int = 1
val list: List<Int> = listOf(1, 2, 3)
```

그런데 원시 타입과 참조 타입이 같다면 코틀린이 항상 객체로 표한하는 걸까요? 그렇게 한다면 비효율적이라 할 수도 있는데요. 실제로도 항상 객체로 표현한다면 비효율적이겠지만 코틀린은 그렇지 않습니다. 실행 시점에 숫자 타입은 가능한 가장 효율적인 방식으로 표현됩니다. 대부분의 경우 코틀린의 Int 타입은 int 타입으로 컴파일 됩니다. 

컴파일이 불가능한 경우는 컬렉션, 제네릭 클래스를 사용하는 경우 뿐입니다. 

<br> <br>

## `널이 될 수 있는 원시 타입: Int?, Boolean? 등`

null 참조를 자바의 참조 타입의 변수에만 대입할 수 있기 때문에 널이 될 수 있는 코틀린 타입은 자바 원시 타입으로 표현할 수 없습니다. 따라서 `코틀린에서 널이 될 수 있는 원시 타입을 사용하면 그 타입은 자바의 래퍼 타입으로 컴파일 됩니다.`

```kotlin
data class Person(val name: String, val age: Int? = null) {
    fun isOlderThan(other: Person): Boolean? {
        if (age == null || other.age == null) return null
        return age > other.age
    }
}
```

위의 코드처럼 age가 null 인지 아닌지 검사해야 합니다. Person 클래스에 선언된 age 프로퍼티 값은 `java.lang.Integer`로 저장됩니다. 

<br>

```kotlin
val listOfInts = listOf(1, 2, 3)
```

제네릭 클래스의 경우 래퍼 타입을 사용합니다. 어떤 클래스의 타입 인자로 원시 타입을 넘기면 코틀린은 그 타입에 대한 박스 타입을 사용합니다. 예를들어 위의 코드는 null 값이나 널이 될 수 있는 타입을 전혀 사용하지 않았지만 만들어지는 리스트는 래퍼인 Integer 타입으로 이뤄진 리스트입니다. 

이렇게 컴파일하는 이유는 자바 가상머신에서 제네릭을 구현하는 방법 때문입니다. `JVM은 타입 인자로 원시 타입을 허용하지 않습니다. 따라서 자바나 코틀린 모두에서 제네릭 클래스는 항상 박스 타입을 사용해야 합니다.`

<br> <br>

## `숫자 변환`

코틀린과 자바의 가장 큰 차이점 중 하나는 숫자를 변환하는 방식입니다. 코틀린은 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않습니다. 결과 타입이 허용하는 숫자의 범위가 원래 타입의 범위보다 넓은 경우조차도 자동 변환은 불가능합니다. 

```kotlin
val i = 1
val l: Long = i  // 컴파일 오류
```

```kotlin
val i = 1
val l: Long = i.toLong();
```

위처럼 직접 변환 메소드를 호출해야 합니다. 코틀린은 Boolean을 제외한 모든 원시 타입에 대한 변환 함수를 제공합니다.(ex: toByte(), toShort(), toChar())

<br> <br>

## `Any, Any?: 최상위 타입`

자바에서 Object가 클래스 계층의 최상위 타입이듯 `코틀린에서는 Any 타입이 모든 널이 될 수 없는 타입의 조상 타입`입니다. 코틀린에서는 Any가 Int 등의 원시 타입을 포함한 모든 타입의 조상입니다.

`Any가 널이 될 수 없는 타임임에 유의해야 합니다.` 따라서 널을 포함하려면 `Any?` 타입을 사용해야 합니다. 코틀린 함수가 자바 바이트코드의 Object로 컴파일 됩니다. `toString`, `equals`, `hashCode` 라는 세 메소드는 Any에 정의된 메소드를 상속해서 모든 코틀린 클래스에서 사용할 수 있습니다. 하지만 wait, notify는 Any에서 사용할 수 없습니다.

<br> <br>

## `Init 타입: 코틀린의 void`

코틀린 Unit 타입은 자바 void와 같은 기능을 합니다. `그렇다면 코틀린의 Unit이 자바 void와 다른 점은 무엇일까요?` Unit은 모든 기능을 갖는 일반적인 타입이며, void와 달리 Unit을 타입 인자로 쓸 수 있습니다. 그리고 `함수형 프로그래밍에서 전통적으로 Unit은 '단 하나의 인스턴스만 갖는 타입'을 의미해 왔고 바로 그 유일한 인스턴스의 유무가 자바 void와 코틀린 Unit을 구분하는 가장 큰 차이입니다.`

<br> <br>

## `Nothing 타입: 이 함수는 결코 정상적으로 끝나지 않는다.`

코틀린에는 결코 성공적으로 값을 돌려주는 일이 없으므로 `반환 값`이라는 개념 자체가 의미 없는 함수가 일부 존재합니다. 예를 들면, 무한 루프를 도는 함수와 같이 정상적으로 끝나지 않는 경우입니다.

```kotlin
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}
```
```
>>> fail("Error occurred")
Exception in thread "main" java.lang.IllegalArgumentException: Error occurred
```

정상적으로 끝나지 않는 함수를 호출하는 코드를 분석하는 경우 함수가 정상적으로 끝나지 않는다는 사실을 알면 유용한데, 이럴 때 코틀린의 `Nothing` 이라는 특별한 반환 타입을 사용하면 됩니다.

<br> <br>

## `널 가능성과 컬렉션`

```kotlin
val result = ArrayList<Int?>    // 널이 될 수 있는 int 값으로 이뤄진 리스트를 만든다.
```

```kotlin
val result1 = ArrayList<Int?>   // 원소가 null 이 될 수 있다.
val result2 = ArrayList<Int>?   // List가 null 일 수 있다. 
```

위 코드에서 `?` 위치에 따라서 조금씩 차이가 있는 것을 볼 수 있습니다. 

<br> <br>

## `읽기 전용과 변경 가능한 컬렉션`

코틀린에서는 `읽기 전용 인터페이스`와 `변경 가능 인터페이스`를 분리했다는 특징이 있습니다. 읽기 전용의 컬렉션 인터페이스는 `kotlin.collections.Collection`이고, 컬렉션의 데이터를 수정하려면 `kotlin.collections.MutableCollection` 인터페이스를 사용해야 합니다. 

코드에서 가능하면 `항상 읽기 전용 인터페이스를 사용하는 것을 일반적인 규칙으로 삼는 것을 권장하고 있습니다.` var, val의 구별과 마찬가지로 컬렉션의 읽기 전용 인터페이스와 변경 가능 인터페이스를 구별한 이유는 프로그램에서 데이터에 어떤 일이 벌어지는지를 더 쉽게 이해하기 위함입니다.

<br>

```kotlin
fun <T> copyElements(source: Collection<T>, target: MutableCollection<T>) {
    for (item in source) {
        target.add(item)
    }
}
```

target에 해당하는 인자로 읽기 전용 컬렉션을 넘길 수 없습니다. 실제 그 컬렉션이 변경 가능한 컬렉션인지 여부와 관계없이 선언된 타입이 읽기 전용이라면 target에 넘기면 컴파일 오류가 발생합니다. 컬렉션 인터페이스를 사용할 때 항상 염두에 둬야 할 핵심은 `읽기 전용 컬렉션이라고 해서 꼭 변경 불가능한 컬렉션일 필요는 없다는 점`입니다.

그리고 어떤 동일한 컬렉션 객체를 가리키는 읽기 전용 컬렉션 타입의 참조와 변경 가능한 컬렉션 타입의 참조가 있는 경우에서 이 컬렉션을 참조하는 다른 코드를 호출하거나 병렬 실행한다면 컬렉션을 사용하는 도중에 다른 컬렉션이 그 컬렉션의 내용을 변경하는 상황이 생길 수 있습니다. 

즉, `읽기 전용 컬렉션이 항상 스레드 안전하지는 않다는 점을 명심해야 합니다.`

<br> <br>

## `코틀린 컬렉션과 자바`

![스크린샷 2021-10-21 오전 12 29 16](https://user-images.githubusercontent.com/45676906/138123684-b84ed635-4fed-4114-ba1c-9b4f74baa8dc.png)

<br>

<img width="769" alt="스크린샷 2021-10-21 오전 12 50 38" src="https://user-images.githubusercontent.com/45676906/138127357-728c3cfc-ca6c-4599-a95f-442a01581da5.png">

`setOf()`, `mapOf()`는 자바 표준 라이브러리에 속한 클래스의 인스턴스를 반환합니다. (최소한 코틀린 1까지는..) 즉, 이것들은 `변경 가능한 클래스 입니다.` 하지만 이 둘이 변경 가능한 클래스라는 사실에 의존하면 안됩니다. 왜냐하면 미래에는 `setOf`, `mapOf`가 불변 컬렉션 인스턴스를 반환할 수 있기 때문입니다.

자바 메소드를 호출하되 컬렉션을 인자로 넘겨야 한다면 따로 변환하거나 복사하는 등의 추가 작업 없이 직접 컬렉션을 넘기면 됩니다. 예를 들어 `java.util.Collection`을 파라미터로 받는 자바 메소드가 있따면 아무 Collection이나 MutableCollection 값을 인자로 넘길 수 있습니다. 

즉, 자바는 읽기 전용 컬렉션과 변경 가능 컬렉션을 구분하지 않으므로, 코틀린에서 읽기 전용 Collection으로 선언된 객체라도 자바 코드에서는 그 컬렉션 객체의 내용을 변경할 수 있습니다.코틀린 컴파일러는 자바 코드가 컬렉션에 대해 어떤 일을 하는지 알 수 없기 떄문에, 코틀린 컴파일러가 변경하는 것을 막을 수 없습니다. 

따라서 컬렉션을 자바 코드에게 넘길 때는 주의해야 하며, 코틀린 쪽 타입이 적절히 자바 쪽에서 컬렉션에게 가할 수 있는 변경의 내용을 반영하게 해야 합니다.

<br> <br>

## `컬렉션을 픗랫폼 타입으로 다루기`

위에서도 플랫폼 타입에 대해 나왔지만, 다시 말하면 자바 코드에서 정의한 타입을 코틀린에서는 플랫폼 타입으로 본다고 했습니다. 플랫폼 타입의 경우 코틀린 쪽에는 널 관련 정보가 없습니다. 
따라서 컴파일러는 코틀린 코드가 그 타입을 널이 될 수 있는 타입이나 널이 될 수 없는 타입 어느 쪽으로든 사용할 수 있게 허용합니다. 

이와 마찬가지로 자바 쪽에서 선언한 컬렉션 타입의 변수를 코틀린에서는 `플랫폼 타입`으로 봅니다. 플랫폼 타입인 컬렉션은 기본적으로 변경 가능성에 대해 알 수 없습니다. 따라서 코틀린 코드는 그 타입을 읽기 전용 컬렉션이나 변경 가능한 컬렉션 어느 쪽으로든 다룰 수 있습니다. 

그래서 컬렉션 타입이 시그니처에 들어간 자바 메소드 구현을 오버라이드하려는 경우 `읽기 전용 컬렉션`과 `변경 가능 컬렉션`의 차이가 문제가 됩니다. 플랫폼 타입에서 널 가능성을 다룰 때처럼 이런 경우에도 오버라이드하려는 메소드의 자바 컬렉션 타입을 어떤 코틀린 컬렉션 타입으로 표현할지 결정해야 합니다.

- 컬렉션이 널이 될 수 있는가?
- 컬렉션의 원소가 널이 될 수 있는가?
- 오버라이드하는 메소드가 컬렉션을 변경할 수 있는가?

<br>

```java
interface FileContentProcessor {
    void processContents(
            File path, 
            byte[] binaryContents, 
            List<String> textContents
    );
}
```

자바 코드의 인터페이스를 코틀린으로 구현하려면 아래의 것들을 선택해야 합니다.

- 일부 파일은 이진 파일이며 이진 파일 안의 내용은 텍스트로 표현할 수 없는 경우가 있으므로 리스트는 `널이 될 수 있습니다.`
- 파일의 각 줄은 널일 수 없으므로 이 `리스트의 원소는 널이 될 수 없습니다.`
- 이 리스트는 파일의 내용을 표현하며 그 내용을 바꿀 필요는 없으므로 `읽기 전용입니다.`

<br>

```kotlin
class FileIndexer : FileContentProcessor {
    override fun processConents(
        path: File, 
        binaryContents: ByteArray?, 
        textContents: List<String>? 
    )
}
```

위의 조건들을 선택하여 코틀린으로 자바 인터페이스를 구현하면 위와 같은 코드로 작성할 수 있습니다. 

<br>

```java
interface DataParser<T> {
    void parseData(String input, List<T> output, List<String> errors);
}
```

위의 코드도 컬렉션이 시그니처에 존재하는 자바 인터페이스인데요. 이번에도 아래의 것들을 선택해야 합니다. 

- 호출하는 쪽에서 항상 오류 메세지를 받아야 하므로 List<String>은 널이 되면 안 됩니다.
- errors의 원소는 널이 될 수도 있습니다. output에 들어가는 정보를 파싱하는 과정에서 오류가 발생하지 않으면 그 정보와 연관된 오류 메세지는 널입니다. 

<br>

```kotlin
class PersonParser : DataParser<Person> {
    override fun parseData(
        input: String, output: 
        MutableList<Person>, errors: 
        MutableList<String?>
    )
}
```

이렇게 컬렉션 자체가 null이 될 수 있는지, 원소가 null이 될 수 있는지 변경될 수 있는지 등등 체크해서 코틀린 코드에서 사용해주면 됩니다.

<br> <br>

## `객체의 배열과 원시 타입의 배열`

코틀린에서 배열을 만드는 방법은 다양합니다. 

- `arrayOf 함수에 원소를 넘기면 배열을 만들 수 있습니다.`
- `arrayOfNulls 함수에 정수 값을 인자로 넘기면 모든 원소가 null이고 인자로 넘긴 값과 크기가 같은 배열을 만들 수 있습니다.`
- `Array 생성자는 배열 크기와 람다를 인자로 받아서 람다를 호출해서 각 배열 원소를 초기화 해줍니다.`

<br>

```kotlin
val letters = Array<String>(26) { i -> ('a' + i).toString() }
println(letters.joinToString(""))
```
```
abcdefghijklmnopqrstuvwxyz
```

람다는 배열 원소의 인덱스를 인자로 받아서 배열의 해당 위치에 들어갈 원소를 반환합니다.