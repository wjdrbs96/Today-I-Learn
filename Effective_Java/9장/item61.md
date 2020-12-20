# `아이템61 : 박싱된 기본 타입보다는 기본 타입을 사용하라`

오토박싱과 오토언박싱 덕분에 두 타입을 크게 구분하지 않고 사용할 수는 있지만, 그렇다고 차이가 사라지는 것은 아닙니다.
둘 사이에는 분명한 차이가 있기 때문에 어떤 타입을 사용하는지는 상당히 중요합니다. 

### `기본타입 vs 박싱 타입`

- 기본 타입은 값만 가지고 있으나, 박싱된 기본 타입은 값에 더해 `식별성`이란 속성을 갖습니다.
- 기본 타입의 값은 언제나 유효하나, 박싱된 기본 타입은 유효하지 않은 값, 즉 null을 가질 수 있습니다.
- 기본 타입이 박싱된 기본 타입보다 시간과 메모리 사용면에서 더 효율적입니다.

<br>

### `문제가 발생하는 예제 코드`

```java
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        Comparator<Integer> naturalOrder = (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);
        System.out.println(naturalOrder.compare(new Integer(42), new Integer(42))); // 1
    }
}
```

위의 코드의 문제점이 있는데 대략 보았을 때는 찾기 쉽지 않습니다. 그런데 위의 코드의 결과를 보면 같은 42를 비교 했는데 결과는 0이 아니라 1이 나오게 됩니다. 

<br>

### `0이 아니라 1이 나오는 이유가 무엇일까요?`

i, j가 참조하는 오토방식된 Integer 인스턴스는 기본 타입 값으로 반환됩니다. 
(이게 정확히 무슨 말인지..)

그런데 여기서 보면 (i == j) 라는 부분에 `== 비교연산자`가 나옵니다. 비교 연산자는 주소의 값으로 비교를 하게 됩니다. 하지만 각자 new 연산자를 이용해서 객체를 할당했기 때문에
주소 값이 다르기 때문에 다르다고 판별을 하는 것입니다. 

`이처럼 박싱된 기본 타입에 == 연산자를 사용하면 오류가 발생합니다.`

<br>

### `문제 해결 코드`

```java
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        Comparator<Integer> naturalOrder = (iBoxed, jBoxed) -> {
            int i = iBoxed, j = jBoxed;
            return (i < j) ? -1 : (i == j ? 0 : 1);
        };
        System.out.println(naturalOrder.compare(new Integer(42), new Integer(42))); // 0
    }
}
```

이처럼 박싱된 Integer 매게변수의 값을 기본 타입 정수로 저장한 다음 비교를 하면 결과가 제대로 나옵니다.

<br>

### `신기한 코드`

```java
public class Test {
    static Integer i;
    public static void main(String[] args) {
        if (i == 42) {
            System.out.println("믿을 수 없군");
        }
    }
}
```

이 코드의 결과는 무엇일까요? 그냥 아무 것도 출력이 안되겠거니 했지만 정답인 `NullPointerException`이 발생합니다. 의외네요..

왜 그런가 봤더니 i의 타입은 Integer이고 초기화 되는 값은 null이기 때문에 그렇습니다. 

`기본 타입과 박싱된 기본 타입을 혼용한 연산에서는 박싱된 기본 타입의 박싱이 자동으로 풀립니다.` 그리고 null 참조를 언박싱하면 NullPointerException이 발생합니다.

```java
public class Test {
    public static void main(String[] args) {
        Long sum = 0L;
        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
``` 

위의 코드를 보면 sum을 박싱된 기본타입 Long으로 선언했기 때문에 for 문을 돌면서 long 타입 i에 의해서 계속 오토박싱이 일어나기 때문에 엄청난 성능 저하가 발생합니다.

<br>

### `박싱된 기본 타입을 언제 써야 할까요?`

- 컬렉션의 원소 키 값으로 쓸 때
- 리플렉션을 통해 메소드를 호출할 때

