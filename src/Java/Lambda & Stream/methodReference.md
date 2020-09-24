## 메소드 레퍼런스

먼저 바로 코드의 예시를 보면서 이해해보자.

<br>

```java
import java.util.function.Function;

public class Test {
    public static void main(String[] args) {
        Function<Integer, String> isToString = (i) -> "number";
    }
}
```

원래 위의 코드와 같이 `Function` 함수형 인터페이스를 사용하고 내부 메소드의 구현체를 `람다식`을 이용하여 작성할 수 있다.
여기서 `람다식` 대신에 `메소드 레퍼런스`라는 것을 이용하여 다른 클래스의 내부 메소드를 구현체로 사용할 수 있다. 

<br>

예를들면 아래와 같다.

```java
public class Greeting {
    private String name;

    public Greeting() {
        
    }

    public Greeting(String name) {
        this.name = name;
    }

    public String hello(String name) {
        return "hello" + name;
    }

    public static String hi(String name) {
        return "hi " + name;
    }
}
```

먼저 `Greeting`이라는 클래스 내부에 `생성자`, `hello`, `hi` 메소드가 존재할 때 위의 메소드를 참조해서 람다식 대신해서 사용해보자.

<br>

사용방법은 아래와 같다.

```java
import java.util.function.UnaryOperator;

public class Test {
    public static void main(String[] args) {
        UnaryOperator<String> isToString = Greeting::hi;
    }
}
```
 
위의 코드와 같이 `::`을 이용해서 호출하면 된다. 이렇게 `::`을 사용하는 것을 `메소드 레퍼런스`라고 한다. `hi`메소드는 `static`이기 때문에 `클래스명::메소드이름`으로 호출할 수 있다.
그러면 `hi`의 메소드 구현체가 람다식을 대신해서 오른쪽에 들어간다고 생각하면 된다.

<br>

```java
import java.util.function.UnaryOperator;

public class Test {
    public static void main(String[] args) {
        Greeting greeting = new Greeting();
        UnaryOperator<String> isToString = greeting::hello;
    }
}
```

그리고 `static`메소드가 아닌 `일반 메소드`를 호출하고 싶다면 위와 같이 `참조변수::메소드이름`으로 호출하면 된다.

<br>

```java
import java.util.function.Function;
import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) {
        Function<String, Greeting> function = Greeting::new;

        Supplier<Greeting> supplier = Greeting::new;
    }
}
```

그리고 이번에는 `Greeting:new`를 이용하여 `생성자`를 이용해볼 것이다. `Function`은 `입력값`, `출력값`이 모두 있고,
`Supplier`는 `입력값`이 없고 `출력값`만 있는 인터페이스이다. 

<br>

```java
import java.util.function.Function;
import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) {
        Function<String, Greeting> function = Greeting::new; // 매게변수가 1개인 생성자

        Greeting greeting = function.apply("gyun");  
        
        
        Supplier<Greeting> supplier = Greeting::new;         // default 생성자
        Greeting greeting1 = supplier.get();         
    }
}
```

따라서 위와 같이 `생성자`와 `new`를 이용하면 `Greeting`의 객체가 만들어져서 사용할 수 있다. 

<br>

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String[] names = {"gyun", "hyunwoo", "bobae"};

        Arrays.sort(names, String::compareToIgnoreCase);

        System.out.println(Arrays.toString(names));    // [bobae, gyun, hyunwoo] 
    }
}
```

그리고 이번에는 일반 클래스의 메소드를 참조할 수 있는 예제이다. 예를들면 `names`라는 배열을 정렬하고 싶을 때,
`Arrays.sort()`를 사용하는데 이 때 두 번째 인자로 `Comparable` 타입을 사용할 수 있다. 

<br>

 `Comparable`도 `@FunctionalInterface`의 어노테이션이 있기 때문에 `함수형 인터페이스`이다. 
따라서 `람다식`을 사용할 수도 있고 위와 같이 `String`클래스에 있는 메소드를 참조해서 사용할 수도 있다.
