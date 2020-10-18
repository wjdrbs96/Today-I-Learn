# 스트림의 연산

스트림이 제공하는 연산은 `중간 연산`과 `최종 연산`으로 분류할 수 있는데, `중간 연산은 연산 결과를 스트림으로 반환`하기 때문에
중간 연산을 연속해서 연결할 수 있다. 반면에 `최종 연산`은 스트림의 요소를 소모하면서 연산을 수행하므로 `단 한번만` 연산이 가능하다.

```
중간연산 : 연산 결과가 스트림인 연산, 스트림에 연속해서 중간 연산할 수 있다.
최종 연산 : 연산 결과가 스트림이 아닌 연산. 스티림의 요소를 소모하므로 단 한번만 가능
```

`중간 연산`은 `map()`과 `flatMap()`, `최종 연산`은 `reduce()`와 `collect()`가 핵심이다. 

<br>

## 지연된 연산

스트림 연산에서 한 가지 중요한 점은 `최종 연산이 수행되기 전까지는 중간 연산이 수행되지 않는다`는 점이다.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("Gyuuny");
        names.add("Choi");
        names.add("Gyun");
        names.add("You");
        
        Stream<String> g = names.stream().filter(s -> {
            System.out.println("==========");
            return s.startsWith("G");
        });
        
    }
}
```

예를들면 위의 코드를 봐보자. `filter` 함수는 `중간연산`이기 때문에 출력은 아무것도 되지 않는다. (예를들면 `dintinct()나 sort()`와 같은 중간 연산을 호출해도 즉각적인 연산이 수행되는 것은 아니라는 것이다.)

<br>

## 스트림 만들기

스트림으로 작업을 하려면, 스트림이 필요하니까 일단 스트림을 생성하는 방법부터 해보자. 스트림의 소스가 될 수 있는 대상은 배열, 컬렉션, 임의의 수 등 다양하다.

<br>

### 컬렉션

컬렉션의 최고 조상인 Collection에 stream()이 정의되어 있다. 그래서 Collection의 자손인 `List`와 `Set`을 구현한 컬렉션 클래스들은 모두 이 메소드로 스트림을 생성할 수 있다.

```
Stream<T> Collection.stream()
```

<br>

예를들어 List로부터 스트림을 생성하는 코드는 다음과 같다.

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> stream = list.stream();   // 스트림의 모든 요소를 출력한다.
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);   // 에러. 스트림이 이미 닫혔다.
    }
}
```

한 가지 주의할 점은 `forEach()`가 스트림의 요소를 소모하면서 작업을 수행하므로 같은 스트림에 `forEach()`를 두 번 호출할 수 없다는 것이다.
그래서 스트림의 요소를 한번 더 출력하려면 스트림을 새로 생성해야 한다. `forEach()`에 의해 스트림의 요소가 소모되는 것이지,
소스의 요소가 소모되는 것은 아니기 떄문에 같은 소스로부터 다시 스트림을 생성할 수 있다.


<br>

## 스트림의 중간연산

### 스트림 자르기 - skil(), limit()

skip()과 limit()은 스트림의 일부를 잘라낼 때 사용하며, 사용법은 아주 간단하다. skip(3)은 처음 3개의 요소를 건너뒤고, limit(5)는 스트림의 요소를 5개로 제한한다.

```
Stream<T> skip(long n)
Stream<T> limit(long maxSize)
```

<br>

예를들어 10개의 요소를 가진 스트림에 `skip(3)`과 `limit(5)`을 순서대로 적용하면 4번쨰 부터 5개의 요소를 가진 스트림이 반환된다.

```java
public class Test2 {
    public static void main(String[] args) {
        IntStream intStream = IntStream.rangeClosed(1, 10);
        intStream.skip(3).limit(5).forEach(System.out::print); // 45678
    }
}
```

<br>

### 스트림의 요소 걸러내기 - filter(), distinct()

distinct()는 스트림에서 중복된 요소들을 제거하고, filter()는 주어진 조건(Predicate)에 맞지 않는 요소를 걸러낸다.

```
Stream<T> filter(Predicate<? super T> predicate)
Stream<T> distinct()
```

<br>

distinct()의 사용방법은 간단하다.

```java
import java.util.stream.IntStream;

public class Test2 {
    public static void main(String[] args) {
        IntStream intStream = IntStream.of(1, 2, 2, 3, 3, 3, 3, 4, 5, 5, 6);
        intStream.distinct().forEach(System.out::print);  // 123456
    }
}
```

<br>

filter()는 매게변수로 Predicate를 필요로 하는데, 아래와 같이 연산결과가 boolean인 람다식을 사용해도 된다.

```java
import java.util.stream.IntStream;

public class Test2 {
    public static void main(String[] args) {
        IntStream intStream = IntStream.rangeClosed(1, 10); // 1 ~ 10
        intStream.filter(i -> i % 2 == 0).forEach(System.out::print);  // 246810
    }
}
```

 


