## `@TestConstructor`

```
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
```

JUnit 5부터는 생성자를 통한 의존관계 주입이 가능해졌다.

AutowireMode.ALL 설정을 통해 @Autowired 어노테이션을 명시하지 않고, private final로 선언된 필드들에 의존관계 주입이 가능하다.

<br>

## `@ParameterizedTest`

반복되는 파라미터를 넣어서 검증해야 하는 경우 `ParameterizedTest`를 사용할 수 있다.

예를들면, `@CsvSource`, `@ValueSource` 어노테이션과 같이 사용할 수 있다.

<br>

## `@CsvSource`

입력값에 따라 결과값이 다른 경우를 테스트 하려면 `@CsvSource`를 사용해 테스트를 할 수 있다. 이름에서 알 수 있듯이 `Csv` 형태처럼 `,`로 구분해서 사용한다.

```kotlin
@CsvSource(
    value = [
        "0, 1",
        "1, 0",
        "1, 1",
    ]
)
@ParameterizedTest
fun `테스트`(test1: Int, test2: Int) {
}
```

<br>

## `@ValueSource`

`@ValueSource` 어노테이션을 사용한 set 테스트에서 모든 조건은 항상 true를 리턴한다.

```kotlin
@ParameterizedTest
@ValueSource(ints = [1, 2, 3])
fun `테스트`(test: Int) {
    assertThat(numbers.contains(element)).isTrue();
}
```

<br>

## `@Nested란?`

`@Nested` 어노테이션을 이용해 중첩 클래스를 이용해 계층적으로 테스트를 작성할 수 있다. 계층적으로 테스트를 작성하게 되면 테스트 그룹화가 쉬워지고, 테스트를 이해하는데 편리할 수 있음

```java
@DisplayName("Test ")
class Test {

    Stack<Object> stack;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        new Stack<>();
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {

        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            assertThrows(EmptyStackException.class, stack::pop);
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        void throwsExceptionWhenPeeked() {
            assertThrows(EmptyStackException.class, stack::peek);
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {

            String anElement = "an element";

            @BeforeEach
            void pushAnElement() {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty() {
                assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                assertEquals(anElement, stack.pop());
                assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked() {
                assertEquals(anElement, stack.peek());
                assertFalse(stack.isEmpty());
            }
        }
    }
}
```
