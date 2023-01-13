## `Qualifier란 무엇일까?`

```kotlin
@Qualifier("testWebClient")
```

스프링에는 `@Qualifier` 어노테이션이 존재한다. 어노테이션의 역할에 대해 알아보자.

<br>

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
@Component
class BarFormatter : Formatter {

    fun format(): String {
        return "Bar Format"
    }
}
```

`하나의 인터페이스 구현체에 2개의 구현체가 있고, 구현체는 모두 Bean으로 등록되어 있다.`는 것을 코드로 보면 위와 같다.

<br>

```kotlin
@Service
class FooService(
    private val formatter: Formatter
)
```

위와 같이 `formatter` Bean으로 주입을 받는다면 스프링에서는 어떤 구현체를 의존 주입시켜야 할 지 알 수 없다.

이럴 때 사용할 수 있는 것이 `@Qualifier` 이다. 즉, 해당 Bean은 `@Qualifier`에 지정한 이름으로 등록하라는 것이다. 

<br>

```kotlin
@Service
class FooService(
    @Qualifier("barFormatter")
    private val formatter: Formatter
)
```

위처럼 `barFormatter`의 Bean을 등록하라고 명시하는 어노테이션이라고 할 수 있다.

<br>

```kotlin
@Service
class FooService(
    private val barFormatter: Formatter
)
```

하지만 이렇게 `Bean 이름을 직접 명시`하면 되기 때문에 `@Qualifier`은 굳이 사용해야 하나? 라는 생각이 있다.

<br>

```kotlin
@SpringBootTest
class CacheApplicationTests(
    private val applicationContext: ApplicationContext
) {

    @Test
    fun contextLoads() {
        applicationContext.let {
            val names = applicationContext.beanDefinitionNames
            for (name in names) {
                println("Bean Name: $name")
            }
        }
    }
}
```
```
...
Bean Name: barFormatter
Bean Name: fooFormatter
Bean Name: fooService
...
```

Bean 이름은 위와 같이 따로 지정하지 않으면 `Class 이름에서 맨 앞 글자가 소문자로 등록이 된다.(ex: FooServce 클래스라면 fooService가 Bean 이름이 된다.)`