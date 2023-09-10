## `Kotlin으로 ConfigurationProperties 사용하기`

오랜만에 간단한 내용에 글로 정리해보려 한다. 정리하려고 하는 내용은 `Spring Boot에서 yml 파일 읽어오기` 정도 이다.

```yaml
spring:
  configuration:
    test: test-value
```

Spring Boot를 사용해본 사람이라면 모두가 알고 있듯이 yml 파일의 값을 읽어올 때 `@Value` or `@ConfigurationProperties`을 사용하여 읽어올 수 있다.

이번 글에서는 `ConfigurationProperties`에 대해서 살짝 알아보려고 한다.

<br>

```kotlin
@Configuration
@EnableConfigurationProperties(ApiConfiguration::class)
class AppConfiguration
```

위처럼 `EnableConfigurationProperties` 위처럼 `ConfigurationProperties` 어노테이션을 활성화 시켜준다.

<br>

```kotlin
@ConfigurationProperties(prefix = "spring.configuration")
class ApiConfiguration(
    val test: String = "",
)
```

```kotlin
class ConfigurationPropertiesTest(
    private val apiConfiguration: ApiConfiguration
) {
    @Test
    fun `ConfigurationProperties를 사용해서 yml 파일 값을 읽어온다`() {
        val test = apiConfiguration.test
        println("Result: $test")
    }
}
```

그리고 `ConfigurationProperties` 어노테이션을 사용해서 `yml` 파일을 읽어올 수 있다.   

<br>

## `Spring Boot 2.2 이후`

`EnableConfigurationProperties`를 사용해도 되지만, Spring Boot 2.2 부터 [ConfigurationPropertiesScan](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/properties/ConfigurationPropertiesScan.html) 어노테이션이 생겼다.

```kotlin
@Configuration
@ConfigurationPropertiesScan("com.example.playground")
class SpringBootApplication
```

이렇게 패키지를 지정해서 좀 더 편하게 사용할 수 있도록 하는 것 같다.

<br>

### `ConstructingBinding 이란?`

`ConfigurationProperties` 사용해서 값을 매핑하려면 `getter`, `setter`가 모두 필요하다.

하지만 `ConstructingBinding` 어노테이션을 사용하면 `immutable 한 필드`에 대해서도 값을 매핑할 수 있다.

```kotlin
@ConfigurationProperties(prefix = "api")
@ConstructorBinding
data class ApiConfiguration(
    val clientId: String,
    val url: String,
    val key: String
)
```

`val`로 존재하는 필드에 대해서도 값을 매핑할 수 있다. 하지만 위처럼 쓰면 에러가 발생할 때가 있다.

```kotlin
@ConfigurationProperties("application")
class CtorBinding @ConstructorBinding constructor(val x: String, val y: String) {
    override fun toString(): String {
        return "CtorBinding(x='$x', y='$y')"
    }
}
```

그러면 위처럼 사용하라고 한다. ([참고 Link](https://github.com/spring-projects/spring-boot/issues/33471#issuecomment-1339237259))

<br>

## `Spring Boot 3.0 이후`

Spring Boot 3.0 문서에 보면 더 이상 [ConstructingBinding](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#constructingbinding-no-longer-needed-at-the-type-level) 어노테이션을 사용하지 않아도 된다고 한다.

```kotlin
@ConfigurationProperties(prefix = "api")
data class ApiConfiguration(
    val clientId: String,
    val url: String,
    val key: String
)
```

즉, 위처럼 사용해도 불변하게 yml 파일 값을 읽어올 수 있다.

<br>

## `Reference`

- [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#constructingbinding-no-longer-needed-at-the-type-level](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#constructingbinding-no-longer-needed-at-the-type-level)
- [https://github.com/spring-projects/spring-boot/issues/33471#issuecomment-1339237259](https://github.com/spring-projects/spring-boot/issues/33471#issuecomment-1339237259)
- [https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/properties/ConfigurationPropertiesScan.html](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/properties/ConfigurationPropertiesScan.html)
- [https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/html/spring-boot-features.html#boot-features-kotlin-configuration-properties](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/html/spring-boot-features.html#boot-features-kotlin-configuration-properties)
- [https://www.baeldung.com/configuration-properties-in-spring-boot](https://www.baeldung.com/configuration-properties-in-spring-boot)