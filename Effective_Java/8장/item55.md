# `아이템55 : 옵셔널 반환은 신중히 하라`

자바 8 전에는 메소드가 특정 조건에서 값을 반환할 수 없을 때 취할 수 있는 선택지는 두 가지가 있었습니다.

- 예외 던지기 => 진짜 예외적인 상황에서만 사용해야 함
- null 반환하기 => 별도의 null 처리 코드가 필요함

<br>

### `이러한 문제를 해결할 대안으로 Optional<T>가 있습니다.`

Optional<T>는 null이 아닌 T 타입 참조를 하나 담거나, 혹은 아무것도 담지 않을 수 있습니다.

옵셔널을 반환하는 메소드는 예외를 던지는 메소드보다 유연하고 사용하기 쉽고, null을 반환하는 메소드보다 오류 가능성이 작습니다.

```java
public class Test {
    public static <E extends Comparable<E>> E max(Collection<E> c) {
        if (c.isEmpty()) {
            throw new IllegalArgumentException("빈 컬렉션");
        }
        
        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }
}
```

위의 코드에 Optional<T>를 적용해보겠습니다. 

```java
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class Test {
    public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
        if (c.isEmpty()) {
            return Optional.empty();
        }
        
        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return Optional.of(result);
    }
}
```

of 메소드에 null을 넣으면 NPE가 발생하기 때문에 null 값도 허용하는 옵셔널을 만들려면 ofNullable 메소드를 사용하면 됩니다.

> `옵셔널을 반환하는 메소드에서는 절대 null을 반환하지 말자.`

<br>

### `스트림 버전`

```java
public class Test {
    public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
        return c.stream().max(Comparator.naturalOrder());
    }
}
```

스트림의 종단 연산 중 상당 수가 옵셔널을 반환합니다. 

<br>

### `null을 반환하거나 예외를 던지는 대신 옵셔널 반환을 선택해야 하는 기준`

```
String lastWordInLexicon = max(words).orElse("단어 없음");
```

위와 같이 기본 값을 서정할 수 있습니다. 

```
Toy myToy = max(toy).orElseThrow(TemperTantrumException::new);
```

상황에 맞는 예외를 던질 수 있고, 예외 팩터리를 건네어서 예외가 발생하지 않으면 생성 비용이 들지 않습니다.

```
Element lastNobleGas = max(Elements.NOBLE_GASES).get();
```

항상 값이 채워져 있다고 가정하면 곧바로 값을 꺼내서 사용하는 방법도 있습니다.

<br>

### `filter, map, flatMap, isPresent 메소드`

```
Optional<ProcessHandle> parentProcess = ph.praent();
System.out.println("부모 PID " + (parentProcess.isPresent() ? String.valueOf(parentProcess.get().pid()) : "N/A"));
```

옵셔널이 채워져 있으면 true 아니면 false를 반환하는 isPresent 메소드

```
Optional<ProcessHandle> parentProcess = ph.praent();
System.out.println("부모 PID " + ph.parent().map(h -> String.valueOf(h.pid())).orElse("N/A"));
```

map을 사용해서 이렇게 더 간단히 다듬을 수 있습니다.

```
streamOfOptionals.filter(Optional::isPresent).map(Optional::get)
```

스트림을 사용하면 옵셔널을 Stream<Optional<T>>로 받아 위와 같이 깔끔하게 구현할 수 있습니다.

```
streamOfOptionals.flatMap(Optinal::stream)
```

flatMap 메소드 사용한 경우

<br>

### `컬렉션, 스트림, 옵셔널 같은 컨테이너 타입은 옵셔널로 감싸면 안된다.`

- 빈 Optional<List<T>>를 반환하기 보다 빈 List<T>를 반환하는 것이 낫다.

<br>

### 어떤 경우에 메소드 반환 타입을 T 대신 Optional<T>로 선언해야 할까?

- 결과가 없을 수 있으며, 클라이언트가 이 상황을 특별하게 처리해야 할 때

박싱된 기본 타입을 담는 옵셔널은 기본 타입 자체보다 무서울 수 밖에 없습니다. 값을 두 겹이나 감싸기 때문에..

그래서 int, long, double 전용 옵셔널이 있습니다. OptinalInt, OptionalLong, OptinalDouble

> 박싱된 기본 타입을 담은 옵셔널을 반환하는 일은 없도록 하자.
> 옵셔널을 컬렉션의 키, 값, 원소나 배열의 원소로 사용하는 게 적절한 상황은 거의 없다.



 


