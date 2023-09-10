## `Kotlin으로 ConfigurationProperties 사용하기`

```yaml
spring:
  configuration:
    test: test-value
```

- yml 파일의 값을 읽어올 때 `ConfigurationProperties`을 사용할 수 있다.

<br>

```kotlin
@Configuration
@EnableConfigurationProperties(ApiConfiguration::class)
class AppConfiguration
```

- `EnableConfigurationProperties` 위처럼 `ConfigurationProperties` 어노테이션을 활성화 시켜줘야 한다.
- 괄호 안에는 어떤 Config 클래스에 대해서 적용할 것인지 넣을 수 있다.

<br>

```kotlin
@ConfigurationProperties(prefix = "spring.configuration")
data class ApiConfiguration(
    val test: String = "",
)
```

<br>

```kotlin
internal class ConfigurationPropertiesTest(
    private val apiConfiguration: ApiConfiguration
): IntegrationTest() {

    @Value("\${spring.configuration.test}")
    private lateinit var configurationTest: String

    override fun cleanUp() {
        TODO("Nothing")
    }

    @Test
    fun `Value를 사용해서 yml 파일의 값을 읽어온다`() {
        assertThat(configurationTest).isEqualTo("test-value")
    }

    @Test
    fun `ConfigurationProperties를 사용해서 yml 파일 값을 읽어온다`() {
        val test = apiConfiguration.test
        println("Result: $test")
    }
}
```

위처럼 yml 파일의 값을 읽어올 수 있다.