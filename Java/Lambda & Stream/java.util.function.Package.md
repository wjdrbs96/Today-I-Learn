## java.util.function 패키지

`java.util.function` 패키지에 일반적으로 자주 쓰이는 형식의 메소드를 함수형 인터페이스로 정의해놓았다. 

|함수형 인터페이스 | 메소드 | 설명 |
|---------|-----------|----------------|
| java.lang.Runnable|void run() | 매개변수도 없고, 반환값도 없음 |
| Supplier<T> | T get() | 매개변수는 없고, 반환값만 있음 |
| Consumer<T> | void accept(T t) | Supplier와 반대로 매개변수만 있고 반환값이 없음 |
| Function<T, R> | R apply(T t) | 일반적인 함수, 하나의 매개변수를 받아서 결과를 반환 |
| Predicate<T> | boolean test(T t) | 조건식을 표현하는데 사용됨. 매개변수는 하나, 반환 타입은 boolean |

메소드 반환값의 유무에 따라 `4개의 함수형 인터페이스`가 정의되어 있다. 

<br>

### `Function` 인터페이스

`Function` 인터페이스에 정의되어 있는 두 개의 `디폴트 메소드`를 살펴보려 한다. 

<br>

### compose 메소드

`compose`메소드의 사용법을 아래의 예시를 보면서 이해해보자.

```java
import java.util.function.Function;

public class Test {
    public static void main(String[] args) {
        Function<Integer, Integer> plus = (i) -> i + 10;
        Function<Integer, Integer> multiply = (i) -> i * 2;

        Function<Integer, Integer> plusAndMultiply = plus.compose(multiply);
        System.out.println(plusAndMultiply.apply(2));   // 14
    }
}
```

`compose` 메소드가 사용된 곳을 보면 `plus.compose(multiply)`를 볼 수 있다. 이 뜻은 `multiply` 참조변수에 해당하는 람다식을
먼저 계산하고 그 결과값으로 `plus` 참조변수의 람다식에 입력값으로 넣겠다는 의미이다. 따라서 결과는 14가 나온다.

<br>

### andThen 메소드

`andThen` 메소드는 `compose` 메소드의 반대라고 생각하면 된다. 

```java
import java.util.function.Function;

public class Test {
    public static void main(String[] args) {
        Function<Integer, Integer> plus = (i) -> i + 10;
        Function<Integer, Integer> multiply = (i) -> i * 2;

        System.out.println(plus.andThen(multiply).apply(2));  // 24
    }
}
```

이번에는 `plus` 람다식을 먼저 실행하고, 그 결과값을 `multiply` 메소드의 입력값으로 넣어 실행하게 된다. 



<br>

### 조건식의 표현에 사용되는 `Predicate`

`Predicate`는 `Function`의 변형으로, 반환타입이 `boolean`이라는 것만 다르다. `Predicate`는 조건식을 람다식으로 표현하는데 사용된다.

```java
import java.util.function.Predicate;

public class Test {
    public static void main(String[] args) {
        Predicate<String> isEmptyStr = s -> s.length() == 0;
        String s = "";

        if (isEmptyStr.test(s)) {
            System.out.println("This is empty String");
        }
    }
}
```

<br>

### 매게변수가 두 개인 함수형 인터페이스

|함수형 인터페이스 | 메소드 | 설명 |
|---------|-----------|----------------|
| BiConsumer<T, U> | void accept(T t, U u) | 두개의 매개변수만 있고, 반환값이 없음 |
| BiPredicate<T, U> | boolean test(T t, U u) | 조건식을 표현하는데 사용됨. 매개변수는 둘, 반환값은 boolean |
| BiFunction(T, U, R> | R apply(T t, U u) | 두 개의 매개변수를 받아서 하나의 결과를 반환 |


<br>

### `UnaryOperator`와 `BinaryOperator`

Function의 또 다른 변형으로 UnaryOperator와 BinaryOperator가 있는데, 매개변수의 타입과 반환타입의 타입이 모두 일치한다는 점만 제외하고는 `Function`과 같다.

|함수형 인터페이스 | 메소드 | 설명 |
|---------|-----------|---------------------|
| UnaryOperator<T> | T apply(T t) | Function의 자손, Function과 달리 매개변수와 결과의 타입이 같다. |
| BinaryOperator<T> | T apply(T t, T t) | BiFunction의 자손, BiFunction과 달리 매개변수와 결과의 타입이 같다. |


<br>

### 컬렉션 프레임워크와 함수형 인터페이스

컬렉션 프레임워크의 인터페이스에 다수의 `default Method`가 추가되었는데, 그 중의 일부는 `함수형 인터페이스`를 사용한다. 


|인터페이스 | 메소드 | 설명 |
|---------|---------------|-------------------|
| Collection | boolean removeIf(Predicate<E> filter | 조건에 맞는 요소를 삭제 |
| List | void replaceAll(UnaryOperator<E> operator | 모든 요소를 변환하여 대체 |
| Iterable | void forEach(Consumer<T> action | 모든 요소에 작업 action을 수행 |
| Map | void forEach(BiConsumer<K, V> action | 모든 요소에 작업 action을 수행 |
|  | void replaceAll(BiFunction<K, V, V> f) | 모든 요소에 치환작업 f를 수행 |

<br>

### 예제

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            list.add(i);
        }

        list.forEach(i -> System.out.print(i + ", "));
        System.out.println();

        // list에서 2 또는 3의 배수를 제거한다.
        list.removeIf(x -> x % 2 == 0 || x % 3 == 0);
        System.out.println(list);

        Map<String, String> hm = new HashMap<>();
        hm.put("1", "1");
        hm.put("2", "2");
        hm.put("3", "3");
        hm.put("4", "4");

        hm.forEach((k, v) -> System.out.print("{" + k + ", " + v + "}, "));
        System.out.println();
    }
}
```
```
0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
[1, 5, 7]
{1, 1}, {2, 2}, {3, 3}, {4, 4}, 
```

위의 코드를 보면 `List`와 `Map`인터페이스의 `함수형 인터페이스`를 사용하는 `default Method`의 사용법을 알 수 있다.

<br>


### 예제2 

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) {
        Supplier<Integer> s = () -> (int)(Math.random() * 100) + 1;
        Consumer<Integer> c = i -> System.out.print(i + " ");
        Predicate<Integer> p = i -> i % 2 == 0;
        Function<Integer, Integer> f = i -> i / 10 * 10;

        List<Integer> list = new ArrayList<>();
        makeRandomList(s, list);
        System.out.println(list);
        printEvenNum(p, c, list);
        List<Integer> newList = doSomeThing(f, list);
        System.out.println(newList);
    }

    static <T> List<T> doSomeThing(Function<T, T> f, List<T> list) {
        List<T> newList = new ArrayList<>(list.size());
        for (T i : list) {
            newList.add(f.apply(i));
        }
        return newList;
    }

    static <T> void printEvenNum(Predicate<T> p, Consumer<T> c, List<T> list) {
        System.out.print("[");
        for (T i : list) {
            if (p.test(i)) { // 짝수만 출력
                c.accept(i);
            }
        }
        System.out.println("]");
    }

    static <T> void makeRandomList(Supplier<T> s, List<T> list) {
        for (int i = 0; i < 10; ++i) {
            list.add(s.get());
        }
    }
}
```

위의 예제를 보면서 `java.util.function` 패키지의 `함수형 인터페이스`의 사용법을 익혀보자. 