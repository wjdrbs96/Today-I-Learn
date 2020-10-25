# Optional이란?

Optinla<T>은 제네릭 클래스로 `T타입의 객체`를 감싸는 래퍼 클래스이다. 그래서 Optional타입의 객체에는 모든 타입의 참조변수를 담을 수 있다.

```java
public final class Optional<T> {
    
    private final T value;   // T타입의 참조 변수
}
```

최종 연산의 결과를 그냥 반환하는게 아니라 Optional객체에 담아서 반환하는 것이다. 이처럼 객체에 담아서 반환을 하면, `반환된 결과가 null인지 매번 if문으로 체크하는
대신 Optional에 정의된 메소드를 통해서 간단히 처리`할 수 있다.

<br>

이제 널 체크를 위한 if문 없이도 NullPointerException이 발생하지 않는 보다 간결하고 안전한 코드를 작성하는 것이 가능해진 것이다.

<br>

## Optional 객체 생성하기

Optional객체를 생성할 때는 `of()` 또는 `ofNullable()`을 사용한다.

```
만일 참조변수의 값이 null일 가능성이 있으면, of()대신 ofNullable()을 사용해야 한다.
of()는 매게변수의 값이 null이면 NullPointerException이 발생하기 때문이다. 
```

<br>

### Example

```java
import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        Optional<String> optVal = Optional.of(null);
        Optional<String> optVal1 = Optional.ofNullable(null);
        System.out.println(optVal);      // NullPointerException 발생
        System.out.println(optVal1);     // Optional.empty
    }
}
```

<br>

## Optional 객체 초기화

Optional<T>타입의 참조변수를 기본값으로 초기화할 때는 empty()를 사용한다. null로 초기화하는 것이 가능하지만, empty()로 초기화 하는 것이 바람직하다.

```java
import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        Optional<String> optVal = null;
        Optional<String> optVal1 = Optional.<String>empty();
    }
}
```

<br>

## Optional 객체의 값 가져오기

Optional 객체의 저장된 값을 가져올 때는 `get()`을 사용한다. 값이 null일 때는 `NoSuchElementException`이 발생하며, 이를 대비해서 `orElse()`로 대체할 값을 지정할 수 있다.

```java
import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        Optional<String> optVal = Optional.ofNullable(null);
        // String s1 = optVal.get();        // optVal의 반환값이 null이면 NuSuchElementException 발생 
        String s2 = optVal.orElse("Optional Test");  
        System.out.println(s1);
    }
}
```

`orElse()`는 null일 때는 매게변수에 있는 값으로 반환을 하게 된다. 그리고 `orElse()`의 변형으로는 null을 대체할 값을 반환하는 람다식을 지정할 수 있는
`orElseGet()`과 null일 때 지정된 예외를 발생시키는 `orElseThrow()`가 있다. 

<br>

## orElse() vs orElseGet()의 차이는?

```java
import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        Optional<String> optVal = Optional.ofNullable("Optional");
        String s1 = optVal.orElse(testMethod());
        System.out.println(s1);

        System.out.println("=============");
        String s2 = optVal.orElseGet(Test2::testMethod);
        System.out.println(s2);
    }

    static String testMethod() {
        System.out.println("출력이 될까요?");
        return "abc";
    }
}
```
```
출력이 될까요?
Optional
=============
Optional
```

`orElse()`를 사용했을 때는 optVal의 값이 null or 값이 있던 간에 `testMethod()`를 무조건 실행한다. 반면에 `orElseGet()`은 optVal이 null일 때만
`testMethod()`를 실행한다. 그리고 `orElseThrow()`는 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 예외를 발생시키는 메소드이다.(대안이 없는 경우에 사용하면 될 것 같다.)

<br>


## isPresent()

`isPresent()`는 Optional 객체의 값이 null이면, false를, 아니면 true를 반환한다. 

```java
import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        if ( Optional.ofNullable(null).isPresent()) {
            System.out.println("Null이 아니네요~");
        }
        else {
            System.out.println("Null 입니다!");
        }
    }
}
```

<br>

## filter(), map(), flatMap()

map()의 연산결과가 Optional<Optional<T>>일 때, flatMap()을 사용하면 Optional<T>를 결과로 얻는다. 만일 Optional객체의 값이 null이면, 이 메소드들은 아무  일도 하지 않는다.

```java
import java.util.Optional;

public class Test2 {
    public static void main(String[] args) {
        Integer integer = Optional.of("123")  // 만약 "123"이 아닌 ""이라면 result = -1
                .filter(x -> x.length() > 0)
                .map(Integer::parseInt).orElse(-1);
        System.out.println(integer.intValue());       // result = 123 (
    }
}
```


