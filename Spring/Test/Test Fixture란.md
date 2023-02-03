## `Test Fixture란?`

Test Fixsture란 자주 사용되는 객체 데이터를 재사용할 수 있도록 만드는 것이다. 

<br>

```
plugins {
    id 'java-test-fixtures'
}
```

위의 `plugin`을 사용하면 `test-fixture`를 사용할 수 있다.

```java
public class StationLineTestFixtures {

    public static StationLine fixture(
        String name,
        String color,
        Long upStationId,
        Long downStationId,
        Integer distance
    ) {
        return StationLine.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
```

이런식으로 사용할 수 있다. 참고로 위의 플로그인을 굳이 추가하지 않고도 이렇게 `Fixture` 형태로 재사용할 수 있게 하는 것은 구축할 수 있다.

<br>

## `Reference`

- [https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures](https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures)