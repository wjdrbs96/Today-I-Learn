## `Mockito annotaion 정리하기`

## `@Mock`

말 그대로 `@Mock`을 사용하면 해당 클래스를 `Mocking` 해서 주입 해준다. 

<br> <br>

## `@InjectMocks`

예를 들어서 `UserService`가 `UserRepository`, `PasswordEncoder`를 참조하고 있다고 가정해보자. 이 때 `UserService`를 객체로 만들려면 `UserRepository`, `PasswordEncoder` 객체들이 필요하다. 

이럴 때 `UserService`에 사용하는 것이 `@InjenctMocks` 이다. 즉, 정리하면 `UserService` 클래스가 만들어질 때 필요한 객체들을 `Mock`으로 주입해주고 `UserService`를 생성해준다.

<br> <br> 

## `@MockBean`

`@Mock`과 비슷한 `@MockBean` 이라는 어노테이션이 있다. 이름이 비슷한것처럼 실제로도 비슷하게 동작한다.

하지만 `@Mockbean`은 경로(org.springframework.boot.test.mock.mockito.MockBean)를 봐도 알 수 있듯이 `@Mock`과는 다르게 spring 영역에 있는 어노테이션이라는 것을 알 수 있다.

`@MockBean`은 스프링 컨텍스트에 mock 객체를 등록하게 되고 스프링 컨텍스트에 의해 `@Autowired`가 동작할 때 등록된 `mock` 객체를 사용할 수 있도록 동작합니다.

즉, `WebMvcTest`로 슬라이스 테스트를 할 때 `MockMvc`를 `@Autowired`로 주입하고 `Service`는 `MockBean`으로 주입해서 사용한다.

<br> <br>

## `@Mock vs @MockBean`

| Mock 종류 | 의존성 주입 Target |
|------|---|
| @Mock | @InjectMocks |
| @MockBean | @SpringBootTest |

- `@Mock`은 `@InjectMocks`에 대해서만 해당 클래스안에서 정의된 객체를 찾아서 의존성을 해결합니다.

- `@MockBean`은 mock 객체를 스프링 컨텍스트에 등록하는 것이기 때문에 `@SpringBootTest`를 통해서 `Autowired`에 의존성이 주입되게 됩니다.

<br> <br>

## `doNothing()`

`void`로 선언된 메소드에 `when()`을 사용하고 싶을 때 사용합니다.

<br> <br>

