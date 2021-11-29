# `스트림의 연산`

스트림이 제공하는 연산은 `중간 연산`과 `최종 연산`으로 분류할 수 있는데, `중간 연산은 연산 결과를 스트림으로 반환`하기 때문에
중간 연산을 연속해서 연결할 수 있다. 반면에 `최종 연산`은 스트림의 요소를 소모하면서 연산을 수행하므로 `단 한번만` 연산이 가능하다.

```
중간연산 : 연산 결과가 스트림인 연산, 스트림에 연속해서 중간 연산할 수 있다.
최종 연산 : 연산 결과가 스트림이 아닌 연산. 스티림의 요소를 소모하므로 단 한번만 가능
```

`중간 연산`은 `map()`과 `flatMap()`, `최종 연산`은 `reduce()`와 `collect()`가 핵심이다. 

<br>

## `지연된 연산`

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

## `스트림 만들기`

스트림으로 작업을 하려면, 스트림이 필요하니까 일단 스트림을 생성하는 방법부터 해보자. 스트림의 소스가 될 수 있는 대상은 배열, 컬렉션, 임의의 수 등 다양하다.

<br>

### `컬렉션`

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

## `스트림의 중간연산`

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

### `스트림의 요소 걸러내기 - filter(), distinct()`

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

<br>

### `정렬 - sorted()`

```
Stream<T> sorted()
Stream<T> sorted<Comparator<? super T> comparator)
```

sorted는 지정된 Comparator로 스트림을 정렬하는데, Comparator대신 int값을 반환하는 람다식을 사용하는 것도 가능하다. Comparator를 지정하지 않으면
스트림 요소의 기본 정렬 기준(Comparable)으로 정렬한다. 단, 스트림의 요소가 Comparable을 구현한 클래스가 아니면 예외가 발생한다.

```java
import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        Stream<String> strStream = Stream.of("dd", "aaa", "cc", "b");
        strStream.sorted().forEach(System.out::print);                               // 오름차순 정렬

        System.out.println();
        Stream<String> strStream1 = Stream.of("dd", "aaa", "cc", "b");
        strStream1.sorted((s1, s2) -> s2.compareTo(s1)).forEach(System.out::printf);  // 역순 정렬
    }
}
```
 
<br>

### `변환 - map()`

스트림의 요소에 저장된 값 중에서 원하는 필드만 뽑아내거나 특정 형태로 변환해야 할 때가 있다. 이 때 사용하는 것이 바로 `map()`이다.
이 메소드의 선언부는 아래와 같으며, `매게변수로 T타입을 R타입으로 변환해서 반환하는 함수를 지정해야한다.`

```
Stream<R> map(Function<? super T, ? extends R> mapper)
``` 

예를 들어 File의 스트림에서 파일의 이름만 뽑아서 출력하고 싶을 때, 아래와 같이 `map()`을 이용하면 File객체에서 파일의 이름(String)만 간단히 뽑아낼 수 있다.

```java
import java.io.File;
import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        Stream<File> fileStream = Stream.of(new File("Ex1.java"), new File("Ex1"), new File("Ex1.txt"));
        Stream<String> stringStream = fileStream.map(File::getName);
        stringStream.forEach(System.out::println);
    }
}
```
```
Ex1.java
Ex1
Ex1.txt
```

<br>

그리고, `map()`도 `filter()`처럼 하나의 스트림에 여러 번 적용할 수 있다. 

```java
import java.io.File;
import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        Stream<File> fileStream = Stream.of(new File("Ex1.java"), new File("Ex1"), new File("Ex1.txt"));
        fileStream.map(File::getName)
                .filter(s -> s.indexOf('.') != -1)   // 확장자가 없는 것은 제외
                .map(s -> s.substring(s.indexOf('.') + 1)) // 확장자만 출력하기 위함
                .map(String::toUpperCase)  // 대문자로 변경 
                .distinct()   // 중복 제거 
                .forEach(System.out::println);  
    }
}
```

<br>

### `flatMap() - Stream<T[]>를 Stream<T>로 변환`

스트림의 요소가 배열이거나 map()의 연산결과가 배열인 경우, 즉 `스트림의 타입이 Stream<T>인 경우, Stream<T>로 다루는 것이 더 편리`할 때가 있다.
그럴 때는 `map()대신 flatMap()을 사용하면 된다.`

```java
import java.util.stream.Stream;

public class Test2 {
    public static void main(String[] args) {
        Stream<String[]> strArrStream = Stream.of(
                new String[]{"abc", "def", "ghi"},
                new String[]{"ABC", "DEF", "GHI"}
        );
    }
}
```

각 요소의 문자열들을 합쳐서 문자열이 요소인 스트림, `즉 Stream<String>으로 만들려면 어떻게 해야할까?`

<br>

먼저 스트림의 요소를 변환해야하니까 일단 map()을 써야할 것이고 여기에 배열을 스트림으로 만들어주는 Arrays.stream(T[])를 함께 사용해보자.

```
Stream<Stream<String>> strStrStream = strArrStream.map(Arrays::stream);
```

예상한 것과 달리, Stream<String[]>을 `map(Arrays::stream)`으로 변환할 결과는 Stream<String>이 아닌, Stream<Stream<String>>이다. 즉, 스트림의 스트림인 것이다.

<br>

각 요소의 문자열들이 합쳐지지 않고, 스트림의 스트림 형태로 되어버렸다. 이 때, 간단히 map()을 아래와 같이 flatMap()으로 바꾸기만 하면 원하는 결과를 얻을 수 있다.

<br>

```
Stream<String> strStream = strArrStream.flatMap(Arrays::stream);
```

