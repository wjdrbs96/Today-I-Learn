# `3.1 코틀린에서 컬렉션 만들기`

```kotlin
fun main() {
    val list = arrayListOf(1, 7, 54);
    println(list.javaClass)

    val set = hashSetOf(1, 7, 53)
    println(set.javaClass)

    val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
    println(map.javaClass)
}
```
```
class java.util.ArrayList
class java.util.HashSet
class java.util.HashMap
```

위의 결과를 보면 코틀린만의 컬렉션 기능을 제공하지 않는다는 것을 알 수 있습니다. 코틀린 컬렉션은 자바 컬렉션과 똑같은 클래스입니다. 하지만 코틀린에서는 자바보다 더 많은 기능을 쓸 수 있습니다. 

<br>

```kotlin
fun main() {
    val strings = listOf("first", "second", "fourteenth")
    println(strings.last())

    val numbers = setOf(1, 14, 2)
    println(numbers.maxOrNull())
}
```
```
fourteenth
14
```

예를들면 위와 같이 리스트의 마지막 원소를 가져오거나 수의 최대 값을 구할 것이 있습니다. 

<br> <br>

## `3.2 함수를 호출하기 쉽게 만들기`

```kotlin
fun main() {
    val list = listOf(1, 2, 3)
    println(list)
}
```
```
[1, 2, 3]
```

위와 같이 컬렉션 or 클래스를 출력하면 자동으로 `toString()` 메소드가 호출되게 되는데요. 이 때 default로 정의되 있는 형태는 `[1, 2, 3]`와 같습니다. 그런데 만약에 `(1;2;3)`와 같은 형태로 출력하고 싶다면 어떻게 해야할까요?

코틀린에서도 표준 라이브러리가 존재하지만, 먼저 직접 함수를 구현해보겠습니다. 

```kotlin
fun main() {
    var list = listOf(1, 2, 3)
    println(joinToString(list, "; ", "(", ")"))
}

fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```
```
(1; 2; 3)
```

위와 같이 `joinToString` 함수를 만들어서 toString을 커스텀해서 출력할 수 있도록 하였습니다. 그런데 매번 함수를 호출할 때 조금 번잡하다는 느낌이 있습니다. 이러한 번잡함을 줄이도록 리팩터링을 해보겠습니다. 

<br> <br>

## `3.2.1 이름 붙인 인자`

위의 `joinToString` 함수의 인자가 여러 개다 보니 어떤 인자가 어떤 역할을 하는지 IDE이나 다른 도움을 받지 않는다면 기억하기 쉽지않다는 단점도 존재합니다. 

```kotlin
fun main() {
    var list = listOf(1, 2, 3)
    println(joinToString(list, separator = "; ", prefix = "(", postfix = ")"))
}
```

이러한 문제에 코틀린에서는 인자에 이름을 명시하여 해결할 수 있습니다. 인자의 이름을 하나라도 명시할꺼라면 전부 다 명시해야 한다는 특징이 있습니다.

<br> <br>

## `3.2.2 디폴트 파라미터 값`

![스크린샷 2021-08-27 오전 12 23 54](https://user-images.githubusercontent.com/45676906/130990789-5972c3b8-f993-492f-8e44-af8835b793a8.png)

자바에서는 일부 클래스에서 오버로딩한 메소드가 너무 많아진다는 문제점이 존재합니다. 대표적으로 `Thread 클래스의 생성자 오버로딩`이 존재하는데요. 이러한 오버로딩은 하위 호환성이나 API 사용자에게 편의를 더하려는 이유들이 있지만 결론적으로 중복 코드라는 단점이 있습니다. 

코틀린에서는 이러한 단점을 해결하기 위해서 `함수 선언에서 파라미터의 디폴트 값을 지정할 수 있으므로 이런 오버로딩을 상당수 피할 수 있습니다.`

```kotlin
fun main() {
    var list = listOf(1, 2, 3)
    println(joinToString(list, separator = "; ", prefix = "(", postfix = ")"))
    println(joinToString(list))
    println(joinToString(list, "; ")) // 매개변수 뒤에 2개 생략
    println(joinToString(list, postfix = ";", prefix = "# ")) // 지정하고 싶은 인자를 이름을 붙여서 순서를 관계없이 지정
}

fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```
```
(1; 2; 3)
1, 2, 3
1; 2; 3
```

함수의 디폴트 파라미터 값은 함수를 호출하는 쪽이 아니라 함수 선언 쪽에서 지정됩니다. 

<br> <br>

## `3.2.3 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티`

자바에서는 모든 코드를 클래스의 메소드로 작성해야 한다는 특징을 가지고 있습니다. 하지만 코틀린에서는 함수를 최상위 수준, 클래스 밖에 위치시킬 수 있습니다. 

`JVM이 클래스 안에 들어있는 코드만을 실행할 수 있기 때문에 코틀린의 단독함수를 실행할 때는 컴파일러가 해당 함수의 새로운 클래스를 정의해줍니다.` 

<br>

### `최상위 프로퍼티`

함수와 마찬가지로 프로퍼티도 파일의 최상위 수준에 놓을 수 있습니다. 

```kotlin
var opCount = 0;

fun performOperation() {
    opCount++
}

fun reportOperationCount() {
    println("Operation performed $opCount times")
}
```

위와 같이 클래스 밖에 존재하는 프로퍼티를 읽어서 출력할 수 있습니다. 

<br> <br>

## `3.3 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티`

기존 코드와 코틀린 코드를 자연스럽게 통합하는 것은 코틀린의 핵심 목표 중에 하나입니다. 만약에 코틀린을 기존 자바 프로젝트에 통합하는 경우에는 코틀린으로 직접 변환할 수 없거나 미처 변환하지 않은 기존 자바 코드를 처리할 수 있어야 합니다. 

이런 기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 편리한 기능을 사용할 때 좋은 것이 `확장 함수` 입니다.
즉, `어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수입니다.`

<br>

```kotlin
fun String.lastChar(): Char = this.get(this.length - 1)
```

확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장할 클래스의 이름을 넣기만 하면 됩니다. `클래스 이름을 수신 객체 타입`이라고 하며, `확장 함수가 호출되는 대상이 되는 값을 수신 객체`라고 부릅니다. 

<br>

```kotlin
println("Kotlin".lastChar())
```

즉, `String이 수신 객체 타입이고 "kotlin"이 수신 객체입니다.` 어떤 면에서 보면 String 클래스의 메소드를 추가한 것과 비슷합니다.

확장 함수 내부에서는 일반적인 인스턴스 메소드의 내부에서와 마찬가지로 수신 객체의 메소드나 프로퍼티를 바로 사용할 수 있습니다. `하지만 확장 함수가 캡슐화를 깨지지 않습니다. 클래스 안에서 저으이한 메스도와 달리 확장 함수 안에서는 클래스 내부에서만 사용할 수 있는 private 멤버나 protected 멤버를 사용할 수 없습니다.`

<br> <br>

## `확장 함수로 유틸리티 함수 정의`

```kotlin
fun main() {
    val list = arrayListOf(1, 2, 3)
    println(list.joinToString(" "))  
}

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }


    result.append(postfix)
    return result.toString()
}
```

확장 함수를 사용해서 `joinToString`을 리팩터링 할 수 있습니다. 

<br> <br>

## `3.3.4 확장 함수는 오버라이드 할 수 없다.`

```kotlin
fun main() {
    val view: View = Button() // 자바에서의 다형성과 비슷
    view.click()
    
}

open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}
```

위와 같이 자바에서 `업캐스팅`처럼 코틀린에서도 상속관계에서 사용할 수 있습니다. 하지만 `확장 함수는 이렇게 작동하지 않습니다.` 확장 함수는 클래스의 일부가 아닙니다. 

이름과 파라미터가 완전히 같은 확장 함수를 기반 클래스와 하위 클래스에 대해 정의해도 실제로는 확장 함수를 호출할 때 수신 객체로 지정한 변수의 정적 타입에 의해 어떤 확장 함수가 호출될지 결정됩니다. 
그 변수의 지정된 객체의 동적인 타입에 의해 확장 함수가 결정되지 않습니다. 

```kotlin
fun main() {
    val view: View = Button()
    view.showOff() // 확장 함수는 정적으로 결정됨
}

fun View.showOff() = println("I'm a view!")
fun Button.showOff() = println("I'm a button!")

open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}
```
```
I'm a view!
```

view가 가리키는 객체의 실제 타입이 Button이지만, 이 경우 view의 타입이 View이기 때문에 무조건 View의 확장 함수가 호출됩니다. 

<br> <br>

## `확장 프로퍼티`

```kotlin
val String.lastChar: Char
    get() = get(length - 1)
```

확장 함수의 경우와 마찬가지로 `확장 프로퍼티`도 일반적인 프로퍼티와 같은데, 단지 수신 객체 클래스가 추가됐을 뿐입니다. 
그리고 `최소한의 Getter는 정의해야 합니다.`

<br>

```kotlin
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }
```

그리고 위와 같이 변경 가능한 확장 프로퍼티로 선언할 수도 있습니다. 

<br> <br>

## `컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브러리 지원`

- vararg 키워드를 사용하면 호출 시 인자 개수가 달라질 수 있는 함수를 정의할 수 있습니다. 
- 중위 함수 호출 구문을 사용하면 인자가 하나뿐인 메소드를 간편하게 호출할 수 있습니다. 
- 구조 분해 선언을 사용하면 복합적인 값을 분해해서 여러 변수에 나눠 담을 수 있습니다. 

<br> <br>

## `3.4.1 자바 컬렉션 API 확장`

위에서 `코틀린 컬렉션은 자바와 같은 클래스를 사용하지만 더 확장된 API를 제공`한다고 했습니다. 

```kotlin
fun main() {
    val view: View = Button()
    view.showOff()

    var strings: List<String> = listOf("first", "second", "fourteenth")
    println(strings.last())

    val numbers: Collection<Int> = setOf(1, 14, 2)
    println(numbers.maxOrNull())
}
```

위와 같이 자바 라이브러리 클래스의 인스턴스인 컬렉션에 대해 코틀린이 새로운 기능을 추가 할 수 있었던 이유는 `last()`, `max()`는 모두 `확장 함수`였던 것입니다.

<br> <br>

## `3.4.2 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의`

```kotlin
fun main(args: Array<String>) {
    val list = listOf("args: ", *args)
    println(list)
}
```

코틀린에서도 자바와 비슷하게 가변 길이 인자를 사용할 수 있습니다. 

<br> <br>

## `3.4.3 값의 쌍 다루기: 중위 호출과 구조 분해 선언`

```kotlin
fun main() {
    val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
    println(1.to("one"))  // 일반적인 방식으로 호출
    println(1 to "one")   // 중위 호출 방식으로 호출
}
```

여기서 to는 코틀린 키워드가 아닙니다. `중위 호출`이라는 방식으로 to라는 일반 메소드를 호출한 것입니다. `인자가 하나뿐인 일반 메소드나 인자가 하나뿐인 확장 함수에 중위 호출을 사용할 수 있습니다.`

함수를 중위 호출에 사용하게 허용하고 싶다면 `infix` 변경자를 함수 선언 앞에 추가해야 합니다. 

```kotlin
public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
```

<br>

```kotlin
fun main() {
    val (number, name) = 1 to "one"
    println("number: $number, name : $name")
}
```

그리고 위와 같이 두 변수를 즉시 초기화할 수 있는데 이것을 `구조 분해 선언`이라고 합니다. 

<br> <br>

## `3.5.1 문자열 나누기`

```java
public class Test {
    public static void main(String[] args) {
        String[] split = "12.345-6.A".split(".");
        System.out.println(Arrays.toString(split)); // []
    }
}
```

자바에서는 `split` 함수로 위와 같이 `마침표(.)`을 분리시키면 `[12, 345-6, A]`와 같은 배열로 반환되는 것이 아니라 빈 배열로 반환이 됩니다. 
split의 구분 문자열은 실제로는 정규식이기 때문입니다. `마침표(.)`는 모든 문자를 나타내는 정규식으로 해석됩니다. 

코틀린에서는 자바의 이러한 혼란스러운 split() 메소드의 매개변수를 String이 아니라 Regex 타입의 값을 받습니다. 

```kotlin  
println("12.345-6.A".split(".")) // [12, 345-6, A]
``` 

<br> <br>

## `코드 다듬기: 로컬 함수와 확장`

코틀린에서는 코드의 중복을 `로컬 함수`를 통해 제거할 수 있습니다. 

```kotlin
class User(val id: Int, val name: String, val address: String)

fun saveUser(user: User) {
    if (user.name.isEmpty()) {
        throw IllegalArgumentException("Can't Save user ${user.id}: empty Name")
    }

    if (user.address.isEmpty()) {
        throw IllegalArgumentException("Can't save user ${user.id}: empty Address")
    }
}
```

위의 코드를 보면 필드가 2개 밖에 안되어서 그렇지 코드의 중복이 많은 것을 볼 수 있습니다. 이것을 코틀린에서는 아래와 같이 리팩토링 할 수 있습니다. 

<br>

```kotlin
class User(val id: Int, val name: String, val address: String)

fun saveUser(user: User) {
    fun validate(user: User, value: String, fieldName: String) {
        if (value.isEmpty()) {
            if (user.name.isEmpty()) {
                throw IllegalArgumentException("Can't Save user ${user.id}: empty $fieldName")
            }
        }
    }
    
    validate(user, user.name, "Name")
    validate(user, user.address, "Address")
    
}
```

중복 코드를 제거하고 좀 더 깔끔하게 코드를 리팩터링 한 것을 볼 수 있습니다. 하지만 `User 객체를 로컬 함수에게 하나하나 전달해야 한다는 것이 단점입니다.`

<br>

```kotlin
class User(val id: Int, val name: String, val address: String)

fun saveUser(user: User) {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            if (user.name.isEmpty()) {
                throw IllegalArgumentException("Can't Save user ${user.id}: empty $fieldName")
            }

        }
    }

    validate(user.name, "Name")
    validate(user.address, "Address")
}
```

위와 같이 로컬 함수는 자신이 속한 바깥 함수의 모든 파라미터와 변수를 사용할 수 있기 때문에 코드를 좀 더 리팩터링 할 수 있습니다. 

<br> 

```kotlin
class User(val id: Int, val name: String, val address: String)

fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            if (name.isEmpty()) {
                throw IllegalArgumentException("Can't Save user ${id}: empty $fieldName")
            }

        }
    }

    validate(name, "Name")
    validate(address, "Address")
}
```

그리고 위와 같이 `확장 함수`를 사용해서 코드를 좀 더 간결하게 만들 수도 있습니다. 이러한 검증 로직의 함수는 User를 사용하는 다른 곳에서만 쓰인다면 User 클래스에 넣는 것이 아니라 위와 같이 `확장 함수`를 사용해서 할 수 있습니다.

확장함수를 사용하면 `validateBeforeSave` 함수에서도 User 클래스의 `Public 프로퍼티`를 접근할 수 있습니다. 