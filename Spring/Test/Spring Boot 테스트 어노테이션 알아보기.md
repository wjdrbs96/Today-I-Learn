## `Spring Boot 테스트 어노테이션 알아보기`

## `@MockBean`

`@MockBean`은 기존에 사용되던 Bean의 껍데기만 가져오고 내부의 구현 부분은 모두 사용자에게 위임한 형태입니다.

```kotlin
@ExtendWith(SpringExtension::class)
@SpringBootTest
class ExampleTests {

    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var exampleService: ExampleService

    @Test
    fun test() {
        given(exampleService.greet()).willReturn("hello");

        val result = userService.makeUse()

        assertThat(result).isEqualTo("user: hello")
    }
}
```

userService -> exampleService를 참조하고 있는 관계인데요. 

위처럼 저는 `userService.makeUse()`에서 `exampleService`을 호출하는 부분을 제외하고 테스트를 진행하고 싶을 수 있습니다. 

이 때 `given().willReturn()`을 사용해서 모킹해서 사용할 수 있습니다.

---(보기 좋게 정리 필요)---

테스트에서 사용되지는 않지만 테스트 하려는 클래스와의 의존 관계로 Bean 등록이 필요한 경우에도 `@MockBean`을 사용할 수 있습니다.

<br>

## `@SpyBean`

@MockBean은 given에서 선언한 코드 외에는 전부 사용할 수 없습니다.

반면에 @SpyBean은 given에서 선언한 코드 외에는 전부 실제 객체의 것을 사용합니다.

이미 존재하는 Bean을 SpyBean으로 Wrapping한 형태라고 생각하시면 됩니다.

예를들어 A 클래스의 a, b 메소드를 사용하는데 a 메소드는 모킹하고 싶고 b 메소드는 실제 로직을 사용하고 싶을 때 사용하는 것입니다.

<br>

```
@Mock
@MockBean
@Spy
@SpyBean
@InjectMocks
@Captor
```

