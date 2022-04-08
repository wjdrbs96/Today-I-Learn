## `@ConfigurationPropertiesScan 이란?`

```java
@ConfigurationPropertiesScan(basePackageClasses = TestBasePackage.class)
public class TestConfig {
}
```

`@ConfigurationPropertiesScan`은 `@ConfigurationProperties`가 등록되어 있는 클래스를 찾아서 `Bean`으로 등록해주는 역할을 합니다. 