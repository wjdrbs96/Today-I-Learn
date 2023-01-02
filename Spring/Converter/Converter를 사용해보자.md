## `Converter를 사용해보자.`

```kotlin
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
class TestKeyWriteConverter : Converter<TestKey, String> {

    override fun convert(testKey: TestKey): String = testKey.toString()
}

@Component
@ReadingConverter
class TestKeyReadConverter : Converter<String, TestKey> {
    override fun convert(rawData: String): TestKey = TestKey.of(rawData)
}
```

- `@WritingConverter`
- `@ReadingConverter`

<br>

두 개의 어노테이션이 존재한다. `springframework.data.convert`에 위치한 녀석이라 `data` 관련 의존성을 하나 추가하면 사용할 수 있다.(ex: data-jpa, data-cassandra 등등)

위처럼 추가하면 `request` 으로 들어오는 값에는 `TestKey` -> `String`으로 바꾸고 `response`로 나가는 값에는 `String` -> `TestKey`로 변환하여 사용할 수 있다.

말 그대로 원하는 형태로 변환하기 위해 사용할 때 매우 유용하다. `DB`에 값을 저장하고 불러올 때도 유용하게 사용할 수 있다.