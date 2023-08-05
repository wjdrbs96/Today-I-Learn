## `Spring Config Location 설정`

Spring Boot 애플리케이션이 실행될 때 자동으로 `application.properties`, `application.yml` 값들을 찾아서 읽는다.

1. 클래스 패스
   - 클래스 패스 루트
   - 클래스 패스 `/config` 패키지
2. 현재 디렉터리
   - 현재 디렉터리
   - 현재 디렉터리 하위 `/config` 패키지
   - 현재 디렉토리 하위 `/config` 하위 디렉토리 

디폴트는 이렇게 된다.

```
spring.config.location
```

위의 설정을 사용하면 디폴트로 정해져 있는 것을 대체할 수 있다.

```kotlin
@SpringBootApplication
class PlaygroundApiApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.location", "classpath:/domain-config/,classpath:/")
    runApplication<PlaygroundApiApplication>(*args)
}
```

이렇게 `Spring Boot Main` 클래스에 `System.setProperty`을 통해서 디폴트 경로를 변경할 수 있다.

<br>

```
spring.config.additional-location
```

만약 디폴트 경로를 대체하는 것이 아니라 디폴트 경로에 추가하고 싶다면 위의 옵션을 사용하면 된다.  

<br>

### `Reference`

- [https://godekdls.github.io/Spring%20Boot/externalized-configuration/](https://godekdls.github.io/Spring%20Boot/externalized-configuration/)
- [https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [https://stackoverflow.com/questions/25855795/spring-boot-and-multiple-external-configuration-files](https://stackoverflow.com/questions/25855795/spring-boot-and-multiple-external-configuration-files)