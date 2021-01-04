# `아이템32: 제네릭과 가변인수를 함께 쓸 때는 신중하라`

`가변인수(varargs)` 메소드와 제네릭은 자바 5 때 함께 추가되었습니다. 함께 추가되어 조화로울 줄 알았지만 아쉽게도 그렇지 않습니다.

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        test("a", "b", "c", "d");
        test("a", "b", "c");
    }

    static void test(String ...test) {
        System.out.println(Arrays.toString(test));
    }
}
```
```
[a, b, c, d]
[a, b, c]
```

가변인수 메소드를 호출하면 가변인수를 담기 위한 배열이 위와 같이 자동으로 하나 만들어집니다. 

> 그런데 내부로 감춰야 했을 이 배열을 그만 클라이언트에게 노출하는 문제가 생겼습니다. 
> 그 결과 varargs 매개변수에 제네릭이나 매개변수화 타입이 포함되면 알기 어려운 컴파일 경고가 발생합니다.

<br>

`실체화 불가 타입`은 런타임에는 컴파일타임보다 타입 관련 정보를 적게 담고 있음을 배웠습니다. (제네릭은 [type erasure](https://github.com/wjdrbs96/Gyunny-Java-Lab/blob/master/Java_God/21%EC%9E%A5/Type%20erasure%EB%9E%80%3F.md) 가 발생하기 때문이죠)

> 실체화 불가 타입이란? E, List<String>, List<E>와 같이 런타임에 타입이 소거가 되는 타입을 뜻합니다. 

대부분의 제네릭은 실체화 불가 타입입니다. 가변인수 메소드를 호출할 때도 varargs 매개변수가 실체화 불가 타입으로 추론되면, 그 호출에 대해서도 경고를 냅니다. 

`매개변수화 타입의 변수가 타입이 다른 객체를 참조하면 힙 오염이 발생합니다.` 힙 오염이라는 단어가 낯설긴 하는데 어떤 것인지 코드를 보면서 알아보겠습니다. 

```java
import java.util.List;

public class Test {
    
    static void dangerous(List<String>... stringLists) {
        List<Integer> intList = List.of(42);
        Object[] objects = stringLists;
        objects[0] = intList;                    // 힙 오염
        String s = stringLists[0].get(0);        // ClassCastException  (형 변환 숨어 있음..)
    }
}
```

메소드에서 형변환하는 코드가 없지만 `ClassCastException`이 발생합니다. 왜 그럴까요? 

이유는 마지막 줄에 컴파일러가 생성한 보이지 않는 형변환이 숨어 있기 때문입니다. 이와 같이 `타입 안정성이 깨지기 때문에 제네릭 varargs 배열 매개변수에 값을 저장하는 것은 안전하지 않습니다.`

<br>

### `제네릭 배열을 만드는 것은 허용하지 않는데 제네릭 가변인수 매개변수를 받는 메소드를 선언할 수 있게 한 이유는 무엇일까요?`

> 그 답은 제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 메소드가 실무에서 매우 유용하기 때문입니다. 

```
Arrays.toList(T... a)
Collections.addAll(Collection<? super T> c, T... elements)
EnumSet.of(E first, E... rest)
```

위에는 자바에서 varargs 매개변수를 받는 메소드의 대표적인 예입니다. 

자바 7 전에는 제네릭 가변인수 메소드의 작성자가 호출자 쪽에서 발생하는 경고에 대해서 해줄 수 있는 것이 없었습니다. 
`그런데 자바 7에서 @SafeVarargs 애너테이션이 추가되어 제네릭 가변인스 메소드 작성자가 클라이언트 측에서 발생하는 경고를 숨길 수 있게 되었습니다.`

> @SafeVarargs 애너테이션은 메소드 작성자가 그 메소드가 타입 안전함을 보장하는 장치입니다. 

<br>

### `메소드가 안전한 게 확실하지 않다면 절대 @SafeVarargs 애너테이션을 달아서는 안됩니다.`

그러면 어떻게 메소드가 안전하지를 확실할 수 있을까요?

> 가변 인수 메소드를 호출할 때 varargs 매개변수를 담는 `제네릭 배열`이 만들어집니다.

- 메소드가 가변인수 배열에 아무 것도 저장하지 않는다.
- 배열의 참조가 밖으로 노출되지 않도록 한다.

위의 조건을 만족하면 안전한 메소드라고 할 수 있습니다. 

이번에는 배열에 아무 것도 저장하지 않았지면 배열의 참조가 밖으로 노출되었을 때 문제점에 대해 알아보겠습니다. 

```java
import java.util.concurrent.ThreadLocalRandom;

public class Test {
    static <T> T[] toArray(T... args) {
        return args;
    }
     
    static <T> T[] pickTwo(T a, T b, T c) {
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0: return toArray(a, b);
            case 1: return toArray(a, c);
            case 2: return toArray(b, c);
        }
        throw new AssertionError();
    }    
}
```

pickTwo 메소드는 T 인스턴스 2개를 담을 varargs 배열을 만드는 코드를 생성합니다. T 타입으로 넘어오는 모든 타입을 수용하려면 
컴파일 타임에 T는 Object[]가 됩니다. 

```
String[] strings = pickTwo("규니", "정규니", "규니규니");
```

그리고 위와 같이 pickTwo 메소드를 호출하면 결과는 어떻게 될까요? 

```
Exception in thread "main" java.lang.ClassCastException
```

위와 같은 에러가 발생합니다. 형변환이 보이지 않는데 에러가 발생하는 이유가 무엇일까요?

> pickTwo 메소드의 반환타입은 Object[] 배열이고, 이것을 strings에 저장하기 위해 String[]로 형변환하는 코드를 컴파일러가 자동으로
> 생성하기 때문입니다. 

따라서 자신의 varargs 매개변수 배열을 그대로 반환하면 `힙 오염`을 이 메소드를 호출한 쪽의 콜스택으로까지 전이하는 결과를 낳을 수 있습니다.

이것은 `제네릭 varargs 매개변수 배열에 다른 메소드가 접근하도록 허용하면 안전하지 않다`는 점을 알 수 있는 에시입니다. 

<br>

### `@SafeVarargs를 작성하는 규칙`

- 제네릭이나 매개변수화 타입의 varargs 매개변수를 받는 모든 메소드에 @SafeVargs를 달기. (안전하지 않은 varargs 메소드를 작상하면 안됩니다.)
- varargs 매개변수 배열에 아무것도 저장하지 않기
- 그 배열을 신뢰할 수 없는 코드에 노출하지 않기

<br>

## 핵심 정리

> 가변인수와 제네릭은 궁합이 좋지 않다. 가변인수 기능은 배열을 노출하여 추상화가 완벽하지 못하고, 배열과 제네릭의 타입 규칙이 서로 다르기 떄문이다.
> 제네릭 varargs 매개변수는 타입 안전하지는 않지만, 허용된다. 메소드에 제네릭 varargs 매개변수를 사용하고자 한다면, 먼저 그 메소드가 타입 안전한지 확인한 다음
> @SafeVarargs 어노테이션을 달자.