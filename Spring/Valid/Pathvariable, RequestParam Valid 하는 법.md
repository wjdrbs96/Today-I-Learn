## `@Pathvariable @Valid로 검증하는 법`

이번 글에서는 `@RequestParam`, `@PathVariable` 어노테이션을 사용해서 값을 받아오는 상황에서 `Valid` 할 수 있는 방법에 대해서 정리해보려 합니다.

```
implementation 'org.hibernate:hibernate-validator:7.0.2.Final'
```

먼저 `gradle`에 위의 의존성을 추가하겠습니다. 그리고 사용하고자 하는 Controller 클래스 이름 위에 `@Validated` 어노테이션을 추가하겠습니다.

<br>

![스크린샷 2021-12-25 오전 2 41 33](https://user-images.githubusercontent.com/45676906/147367416-5bb66283-eb20-496d-b8a8-7e59915d2790.png)

그리고 검증하고자 하는 `API`에서 `@Min`, `@Max` 등등 필요한 어노테이션을 사용하면 됩니다. 

<br>

![스크린샷 2021-12-25 오전 2 45 19](https://user-images.githubusercontent.com/45676906/147367503-1358c898-0488-43f8-8ab3-ce76ab5bb8de.png)

검증 단계에서 문제가 생기면 `ConstraintViolationException` 예외가 생기는데 위와 같이 `ExceptionHandler`로 등록해서 사용하면 됩니다. 

<br>

## `Reference`

- [https://www.baeldung.com/spring-validate-requestparam-pathvariable](https://www.baeldung.com/spring-validate-requestparam-pathvariable)