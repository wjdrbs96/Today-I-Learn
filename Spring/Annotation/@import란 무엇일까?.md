## `@import란 무엇일까?`

거의 사용해보지 않은 어노테이션이라 간단하게 어떻게 사용하는 것인지 정도만 정리해놓으려 합니다.

이름에서 알 수 있듯이 import 하는 어노테이션인데요.

```java
@ComponentScan(basePackageClasses = TestBasePackage.class)
@ConfigurationPropertiesScan(basePackageClasses = TestBasePackage.class)
@Import({
	AConfig.class,
    BConfig.class
})
public class TestConfig {
}
```

멀티 모듈인 api, core 모듈이 존재하는데 core 모듈에서 컴포넌트 스캔을 해야 하는 상황인데요. core 모듈에서 여러 개의 Config 파일을 컴포넌트 스캔하고 싶다면 위와 같이 `import 어노테이션`을 사용해서 할 수 있습니다.

이처럼 말 그대로 1개 이상의 `@Configuration` 클래스들을 `import` 해서 사용하는 용도로 쓰는 어노테이션 입니다.
