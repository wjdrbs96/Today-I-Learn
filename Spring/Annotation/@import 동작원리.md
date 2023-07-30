## `@import란 무엇일까?`

1. `설정의 재사용성과 모듈화`: @Import 어노테이션을 사용하여 다른 설정 파일이나 Java Config 클래스에 있는 설정들을 현재의 클래스로 가져오면, 설정들을 재사용하고 모듈화하는 할 수 있습니다. 이렇게 함으로써 코드를 보다 구조적으로 유지할 수 있으며, 기능에 따라 설정을 분리하여 관리할 수 있니다.

2. `더 나은 구조화와 관리`: 여러 개의 설정 클래스를 사용하고, 각 설정 클래스가 서로를 참조하는 경우가 발생할 수 있는데, 이 때 @Import 어노테이션을 사용하여 설정을 하나의 클래스로 가져오면, 애플리케이션의 구조가 더욱 명확해지고, 설정 간의 종속성이 줄어들 수 있습니다. 이로 인해 애플리케이션의 유지보수가 쉬워집니다.


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

이처럼 말 그대로 1개 이상의 `@Configuration` 클래스들을 `import` 해서 사용하는 용도로 쓰는 어노테이션 입니다.
