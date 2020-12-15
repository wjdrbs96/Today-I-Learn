# `아이템32: 제네릭과 가변인수를 함께 쓸 때는 신중하라`

- 가변인수: 일단 거의 사용해 본 기억이 없다. 
- 실체화 불가 타입: 제너릭을 말했던 건가,,
- 힙 오염을 시킨다는 것이 구체적으로 어떤 것일까? (객체끼리 엮어서 가비지 컬렉터가 회수할 때 문제가 된다는 것인가)
    - 힙 오염이 뭔가 계속 제너릭 형변환으로 ClassCastException을 던지는 것을 말하는데 이런 것과 관련이 있을까

<br>

### 질문: 제네릭 배열은 직접 생성할 수 있게 허용하지 않으면서 제네릭 varargs 매게변수를 받는 메소드를 선언할 수 있게한 이유는 무엇일까요?

```java
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> stringLists = new List<String>[1];
    }
}
```

위의 코드는 컴파일 에러가 발생합니다. 

```java
import java.util.List;

public class Test {
    static void dangerous(List<String>... stringLists) {
        List<Integer> inList = List.of(42);
        Object[] objects = stringLists;
        objects[0] = inList;                     // 힙 오염 발생
        String s = stringLists[0].get(0);        // ClassCastException
    }
}
```

이렇게 두 코드의 차이를 둔 이유는 제네릭이나 매게변수화 타입의 varargs 매게변수를 받는 메소드가 실무에서 매우 유용하기 때문입니다. 

<br>

- @SafeVarargs 애너테이션은 메서드 작성자가 그 메소드가 타입 안전함을 보장하는 장치입니다. (메서드가 안전한게 아니라면 절대로 이 어노테이션을 사용하면 안된다.)

<br>

## 결론

- 정확인 이해는 가지 않지만 => 가변인수와 제네릭은 궁합이 좋지 않은 정도인 것 같다.

