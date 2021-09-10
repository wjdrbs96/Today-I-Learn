# `5장: 람다로 프로그래밍`

## `람다와 컬렉션`

사람들로 이뤄진 리스트가 있고 그 중에 가장 연장자를 찾고 싶은 코드를 작성해보겠습니다. 

```kotlin
data class Person(val name: String, val age: Int)

fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

fun main() {
    findTheOldest(listOf(Person("Alice", 29), Person("Bob", 31)))
}
```

위의 코드는 연산자 등호 방향을 잘못 한다와 같이 실수할 여지가 존재하는 불안한 코드인데요.

<br>

```kotlin
fun main(args: Array<String>) {
    runApplication<AusgApplication>(*args)
    var people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxByOrNull { it.age })
}
```

위와 같이 `maxBy`를 사용하면 훨씬 간단하게 `가장 나이가 많은 사람`을 구할 수 있습니다.

<br> <br>

## `람다 식의 문법`

람다는 값처럼 여기저기 전달할 수 있는 동작의 모음입니다. `코틀린 람다 식은 항상 중괄호로 둘러싸여 있습니다.(그리고 인자 목록 주변에 괄호가 없다는 사실을 기억하기!)`

```kotlin
val sum = { x: Int, y: Int -> x + y }
println(sum(1, 2))

people.maxByOrNull ( { p: Person -> p.age } )
people.maxByOrNull() { p: Person -> p.age }   // 마지막 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼기 가능
people.maxByOrNull { p: Person -> p.age }     // 함수의 인자가 람다식이 유일하다면 괄호 생략 가능
```

그리고 코틀린에서는 람다 식을 다양하게 사용할 수 있습니다. 

<br>

```kotlin
var people = listOf(Person("Alice", 29), Person("Bob", 31))
var names = people.joinToString(separator = " ", 
                    transform = { p: Person -> p.name })
   
println(names)
```

위와 같은 `joinToString` 함수를 사용하는 코드가 있는데, 마지막 인자가 람다식인 것을 볼 수 있습니다. 이것을 위에서 말한 코틀린 람다식 특징을 적용하면 코드를 더 깔끔하게 정리할 수 있습니다.

<br>

```kotlin
var names = people.joinToString(" ") { p: Person -> p.name }
```

마지막에 존재하는 람다식을 밖으로 빼니까 코드가 훨신 깔끔해진 것을 볼 수 있습니다. 하지만 이름이 없기 때문에 람다의 용도를 알아보기가 더 힘들다는 단점이 존재합니다.

<br> <br>

## `람다 파라미터 타입 제거하기`

```kotlin
people.maxByOrNull { p: Person -> p.age }  // 파라미터 타입을 명시
people.maxByOrNull { p -> p.age }          // 파라미터 타입을 생략 (컴파일러가 추론)
people.maxByOrNull { it.age }              // 디폴트 이름인 it으로 바꾸면 람다 식을 더 간단히 가능
```

로컬 변수처럼 컴파일러는 람다 파라미터의 타입도 추론할 수 있습니다. 람다의 파라미터 이름을 디폴트 이름인 it으로 바꾸면 람다 식을 더 간단하게 만들 수 있습니다. 

<br>

```kotlin
var name =  { p -> p.age }          // 컴파일 에러
var name =  { p: Person -> p.age }  // 람다를 변수에 저장할 때는 타입 필요
```

람다식을 변수에 저장할 때는 파라미터의 타입을 명시해야 합니다.

<br>

```kotlin
val sum = { x: Int, y: Int -> 
    println("Gyunny Kotlin Study")
    x + y
}
```

람다식은 여러 줄이 될 수도 있는데 마지막 줄이 람다식의 결과가 됩니다. 

<br> <br>

## `현재 영역에 있는 변수에 접근`

```kotlin
fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}

fun main() {
    val errors = listOf("403 Forbidden", "404 Not Found")
    printMessageWithPrefix(errors, "Error")

}
```
```
Error 403 Forbidden
Error 404 Not Found
```

람다를 함수 안에서 정의하면 함수의 파라미터 뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 모두 사용할 수 있습니다.  

<br>

```kotlin
fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++                   // 변수 수정 가능 
        } else if (it.startsWith("5")) {
            serverErrors++                   // 변수 수정 가능 
        }
    }
}
```

그리고 자바와 다른 점 중 중요한 한 가지는 코틀린 람다 안에서는 파이널 변수가 아닌 변수를 접근하여 변경할 수 있습니다. 람다 안에서 사용하는 외부 변수를 `람다가 포획한 변수` 라고 부릅니다.

`기본적으로 함수 안에 정의된 로컬 변수의 생명주기는 함수가 반환되면 끝납니다. 하지만 어떤 함수가 자신의 로컬 변수를 포획한 람다를 반환하거나 다른 변수에 저장한다면 로컬 변수의 생명주기와 함수의 생명주기가 달라질 수 있습니다.` 
`포획한 변수가 있는 람다를 저장해서 함수가 끝난 뒤에 실행해도 람다의 본문 코드는 여전히 포획한 변수를 읽거나 쓸 수 있습니다.` 

<br> <br>

### `변경 가능한 변수 포획하기`

자바에서는 final 변수만 포획할 수 있습니다. 

```kotlin
class Ref<T>(var value: T)       // 변경 가능한 변수를 포획하는 방법을 보여주기 위한 클래스
val counter = Ref(0)             // 공식적으로는 변경 불가능한 변수를 포획했지만 
val inc = { counter.value++ }    // 그 변수가 가리키는 객체의 필드 값을 바꿀 수 있다.
```

```kotlin
var counter = 0
val inc = { counter++ }
```

첫 번째 예제는 두 번쨰 예제가 작동하는 내부 모습을 보여줍니다. `람다가 파이널 변수(val)을 포획하면 자바와 마찬가지로 그 변수의 값이 복사됩니다.` 하지만 람다가 `변경 가능한 변수(var)를 포획하면 변수를 Ref 클래스 인스턴스에 넣습니다.`


<br> <br>

## `멤버 참조`

::를 사용하는 식을 `멤버 참조`라고 부릅니다. 멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어줍니다. 

```kotlin
people.maxByOrNull(Person::age)
people.maxByOrNull { p -> p.age }
people.maxByOrNull { it.age }
```

<br> <br>

### `생성자 참조`

```kotlin
data class Person(val name: String, val age: Int)
val createPerson = ::Person
val p = createPerson("Alice", 29)
```

:: 뒤에 클래스 이름을 넣으면 `생성자 참조`를 사용할 수 있습니다. 

<br> <br>

## `필수적인 함수: filter와 map`

```kotlin
val list = listOf(1, 2, 3, 4)
println(list.filter { it % 2 == 0 })
```

`filter 함수`는 이름에서 알 수 있듯이 걸러내는 작업인데 위와 같이 사용하면 됩니다. 

<br>

```kotlin
val list = listOf(1, 2, 3, 4)
println(list.map { it * it })
```
```
[1, 4, 9, 16]
```

원소를 변환하려면 map 함수를 사용해야 합니다. 

<br> <br>

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.filter { it.age > 30 }.map(Person::name))
```

30살 이상인 사람의 이름을 출력을 `filter, map`을 사용해서 간단하게 할 수 있습니다. 

<br> <br>

## `all, any, count, find: 컬렉션에 술어 적용`

컬렉션에 대해 자주 수행하는 연산으로 컬렉션의 모든 원소가 어떤 조건을 만족하였는지 판단하는 연산이 이씃ㅂ니다. 코틀린에서는 `all`과 `any`가 이런 연산입니다.

```kotlin
val canBeInClub27 = { p: Person -> p.age <= 27 }

val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.all(canBeInClub27))  // false

val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.any(canBeInClub27))  // true 

println(people.count(canBeInClub27))  // 1

println(people.find(canBeInClub27))   // Person(name=Alice, age=29)
```

모든 list 원소가 `canBeInClub27` 조건에 알맞는지 확인할 때 사용하는 것이 `all` 입니다. 그리고 `any`는 하나라도 속한다면 true를 반환합니다. 그리고 count, find 모두 어떤 역할을 하는지 이름에서 유추할 수 있습니다. 

<br> <br>

## `groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경`

이름에서 알 수 있듯이 `groupBy`를 통해서 어떤 특성에 따라 그룹으로 나눌 때 사용합니다. 

```kotlin
println(people.groupBy{ it.age } )  // {29=[Person(name=Alice, age=29)], 31=[Person(name=Bob, age=31)]}
```

<br> <br>

## `flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리`

```kotlin
var strings = listOf("abc", "def")
println(strings.flatMap { it.toList() })  // [a, b, c, d, e, f]
```

toList 함수를 문자열에 적용하면 그 문자열에 속한 모든 문자로 이뤄진 리스트가 만들어집니다. 

![스크린샷 2021-09-10 오전 2 24 12](https://user-images.githubusercontent.com/45676906/132733225-7c549819-b9e8-4dfa-a8ba-e126b3dd94df.png)

flatmap을 사용하면 위와 같이 적용됩니다. 

<br> <br>

## `지연 계산(lazy) 컬렉션 연산`

위에서 map이나 Filter 같은 몇 가지 컬렉션 함수는 결과 컬렉션을 `즉시 생성` 합니다. 즉, 컬렉션 함수를 연쇄하면 매 단계마다 계산 중간 결과를 새로운 컬렉션에 임시로 담는다는 말입니다. `시퀀스를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 얀산을 연쇄할 수 있습니다.`

```kotlin
people.map(Person::name).filter { it.startsWith("A") }
```

map, filter 모두 list를 반환한다고 하는데요. 이는 연쇄 호출로 인해서 list를 2개 만든다는 뜻입니다. 원본 리스트가 개수가 적다면 큰 문제가 되지 않지만, 개수가 많다면 성능상 이슈가 있을 수 있습니다. 

```kotlin
people.asSequence()
    .map(Person::name)
    .filter { it.startsWith("A") }
    .toList()
```

이 때 `asSequence()`를 통해서 시퀀스를 사용하면 중간 결과를 저장하는 컬렉션이 생기지 않게 할 수 있습니다.

<br> <br>

## `시퀀스 연산 실행: 중간 연산과 최종 연산`

- 중간 연산: 다른 시퀀스를 반환합니다.
- 최종 연산: 결과를 반환합니다.

<br>

![스크린샷 2021-09-10 오전 2 38 04](https://user-images.githubusercontent.com/45676906/132735222-d6552fd3-5bce-4281-8fc3-a29301736767.png)

```kotlin
listOf(1, 2, 3, 4).asSequence()
    .map {it * it }
    .filter { it % 2 == 0 }
```

`map`, `filter`는 `중간 연산`이기 때문에 실행하면 아무 것도 출력되지 않습니다. 

<br>

```kotlin
listOf(1, 2, 3, 4).asSequence()
    .map {it * it }
    .filter { it % 2 == 0 }
    .toList()
```

`toList()`를 통해서 `최종 연산`을 호출하면 연기됐던 모든 계산이 수행됩니다. 

<br>

## `알아두기!`

컬렉션은 map 함수를 각 원소에 대해 먼저 수행해서 새 시퀀스를 얻고, 그 시퀀스에 대해 다시 filter를 수행할 것입니다. 하지만 시퀀스에 대한 map과 filter는 각 원소에 대해 순차적으로 적용됩니다. 즉, 첫 번째 원소가 처리되고, 다시 두 번재 원소가 처리 됩니다.

```kotlin
val toList = listOf(1, 2, 3, 4).asSequence()
    .map { it * it }
    .find { it > 3 }

println(toList) // 4
```

![스크린샷 2021-09-10 오전 3 00 18](https://user-images.githubusercontent.com/45676906/132738479-70786ff3-60cb-4644-9b79-0e145b1432e0.png)

시퀀스를 호출하면 Find 호출이 원소를 하나씩 처리하기 시작합니다. 즉, 4에서 이미 답을 찾았기 때문에 3, 4는 더 이상 계산하지 않고 연산을 끝내게 됩니다.

<br> <br>

## `자바 함수형 인터페이스 활용`

<br> <br>

## `수신 객체 지정 람다: with와 apply`

자바의 람다에는 없는 코틀린 람다의 독특한 기능이 있는데, 그것은 `수식 객체를 명시하지 않고 람다의 본문 안에서 다른 객체의 메소드를 호출할 수 있게 하는 것` 입니다. 그런 람다를 `수식 객체 지정 람다` 라고 합니다. 

<br> <br>

### `with 함수`

```kotlin
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }

    result.append("\nNow I know the alphabet!")
    return result.toString()
}

fun main() {
    println(alphabet())
}
```

위의 코드를 보면 result를 반복해서 사용하고 있는 것을 볼 수 있습니다. 코드가 짧아서 괜찮다고 느낄지도 모르지만, 코드가 길고 더욱 자주 사용된다면 귀찮다는 느낌을 얻을 수 있습니다.

<br>

```kotlin
fun alphabet(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)     // this 사용
        }
        
        append("\nNow I know the alphabet!")  // this 생략 가능
        this.toString()
    }
    
}
```

위의 코드는 `with`를 사용했을 때의 코드입니다. with 함수는 첫 번째 인자로 받은 객체를 두 번째 인자로 받은 람다의 수신 객체로 만듭니다. 인자로 받은 람다 본문에서는 this를 사용해 그 수신 객체에 접근할 수 있습니다. 

<br>

```kotlin
fun alphabet() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        this.append(letter)
    }

    append("\nNow I know the alphabet!")
    toString()
}
```

위의 코드를 좀 더 리팩터링 해보면 식을 본문으로 하는 함수로 표현할 수 있습니다. with가 반환하는 값은 람다 코드를 실행한 결과며, 그 결과는 람다 식의 본문에 있는 마지막 식의 값입니다. 하지만 `때로는 람다의 결과 대신 수신 객체가 필요한 경우도 있습니다. 그럴 때는 apply 함수를 사용할 수 있습니다.`

<br> <br>

### `apply 함수`

apply 함수는 거의 with랑 같습니다. 유일한 차이는 `apply는 항상 자신에게 전달된 수신 객체를 반환한다는 것`입니다. 

```kotlin
fun alphabet() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        this.append(letter)
    }

    append("\nNow I know the alphabet!")
}.toString()
```

위의 코드는 apply를 사용해서 `alphabet` 함수를 리팩터링한 것입니다. 이 함수에서 apply를 실행한 결과는 StringBuilder 객체입니다. 그리고 마지막에 toString을 호출해서 String 객체를 얻을 수 있습니다.

apply 함수는 객체의 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화해야 하는 경우 유용합니다. 자바에서는 보통 별도의 Builder 객체가 이런 역할을 담당합니다.