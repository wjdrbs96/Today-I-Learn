## `@Primary란?`

[@Qualifier](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Spring/Annotation/%40Qualifier%20%EB%9E%80%20%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C.md) 어노테이션도 같이 보면 좋을 것 같다.

```kotlin
interface Formatter {}
```

```kotlin
@Component
class FooFormatter : Formatter {

    fun format(): String {
        return "Foo Format"
    }
}
```

```kotlin
@Primary
@Component
class BarFormatter : Formatter {

    fun format(): String {
        return "Bar Format"
    }
}
```

위처럼 하나의 인터페이스가 있고 구현체의 여러 개 Bean 타입이 존재하는 상황이다.

<br>

```kotlin
@Service
class FooService(
    private val formatter: Formatter
)
```

위와 같이 인터페이스를 의존 주입할 때 어떤 구현체 Bean을 등록해야 하는지 모르기 때문에 `@Primary` 어노테이션을 사용해서 해당 구현체를 주입해달라고 명시하는 것이다.

즉, `@Primary`가 존재하는 `BarFormatter` 타입의 구현체가 주입이 된다. 