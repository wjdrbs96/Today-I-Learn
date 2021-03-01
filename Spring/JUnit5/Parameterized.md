# `Parameterized 란?`

```java
public class SetTest {
    private Set<Integer> numbers;

    @BeforeEach
    void setUp() {
        numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
    }
}
```

위와 같은 초기 Set이 있을 때 `contains()` 메소드를 이용해서 원소들이 들어있는지 여부를 테스트 하는 코드를 작성한다고 가정해보겠습니다.

```java
    @Test
    void contains() {
        assertThat(numbers.contains(1)).isTrue();
        assertThat(numbers.contains(2)).isTrue();
        assertThat(numbers.contains(3)).isTrue();
    }
```

위와 같이 작성을 할 수 있습니다. 하지만 위의 코드는 단점이 존재합니다. 어떤 단점이 존재할까요? `코드가 중복된다는 것입니다.`

코드의 중복을 어떻게 막을 수 있을까요? 바로 `@ParameterizedTest()`를 이용하면 됩니다. 

<br>

## `@ParameterizedTest 사용법`

```java
    @ParameterizedTest(name = "ParameterizedTest를 이용하여 중복코드 제거하기")
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("요구사항2 - contains 메소드 활용하여 값이 존재하는지 여부 판단")
    void containsTest(int number) {
        assertTrue(numbers.contains(number));
    }
```

위와 같이 `ValueSource`를 사용하여 배열을 만들어주면 배열의 원소가 하나씩 number 매개변수에 값을 할당해서 확인하는 과정이 일어납니다. 그렇기에 코드의 중복을 막을 수 있습니다.

-  1, 2, 3 값은 contains 메소드 실행결과 true, 4, 5 값을 넣으면 false 가 반환되는 테스트를 하나의 Test Case로 구현해보겠습니다. 

이 때는 `@CsvSource`를 이용할 수 있습니다. 

```java
    @ParameterizedTest(name = "@CsvSource를 이용하여 number:expected 관계 정의하기")
    @CsvSource(value = {"1:true", "2:true", "3:true", "4:true", "5: false"}, delimiter = ':')
    void containNumberAndExpected(int number, boolean expected) {
        assertThat(numbers.contains(number)).isEqualTo(expected);
    }
```

위와 같이 `@CsvSource`을 이용해서 `value`에 key-value로 넣어놓으면 차레대로 number, expected에 들어가게 됩니다. 

좀 더 유연하게 테스트 코드를 작성할 수 있습니다. 