# `Java 8에 추가된 것들은?`

자바 8에서 변경된거나 새로 추가된 것이나 변경된 것이 많습니다. 이번 글에서는 일부분만 정리를 해보려 합니다. 

먼저 `optional`에 대해서 알아보겠습니다. 

<br>

## `Optional이란?`

자바 8 전에는 메소드가 특정 조건에서 값을 반환할 수 없을 때 취할 수 있는 선택지는 두 가지가 있었습니다.

- 예외 던지기 => 진짜 예외적인 상황에서만 사용해야 함
- null 반환하기 => 클라이언트에서 별도의 null 처리 코드가 필요함

<br>

### `이렇게 null 처리를 보다 간편하게 하기 위해서 Optional<T>이 나왔습니다.`

```java
public final class Optional<T> {

    private Optional() {
        this.value = null;
    }
}
```

Optional을 보면 `final` 클래스인 것을 볼 수 있습니다. 이것은 다른 객체의 부모 클래스가 될 수 없음을 뜻합니다. 그리고 생성자의 접근지정자가 private 인 것을 보면
객체도 생성할 수 없음을 알 수 있습니다. 

<br>

## `Optional 객체를 만드는 법`

그래서 Optional을 API를 보면 `of()`, `empty()`, `ofNullable()`와 같이 객체를 반환하는 것들이 있습니다. 

```java
public final class Optional<T> {

    private static final Optional<?> EMPTY = new Optional<>();

    public static<T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    public static<T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<? super T> action) {
        if (value != null) {
            action.accept(value);
        }
    }
}
```

객체를 만드는 방법의 예시를 3가지를 알아보겠습니다. 

```java
import java.util.Optional;

public class Test {
    public static void main(String[] args) {
        Optional<Object> emptyString = Optional.empty();                // 1
        Optional<String> nullableString = Optional.ofNullable(null);    // 2     
        Optional<String> gyuunyString = Optional.of("Gyuuny");   // 3
    }
}
```

1. 데이터가 없는 Optional 객체를 생성하려면 이와 같이 empty() 메소드를 사용합니다. 
2. 만약 null이 추가될 수 있는 상황이라면 ofNullable() 메소드를 사용합니다.
3. 반드시 null이 아니고 데이터가 있다고 보장할 수 있다면 of() 메소드를 사용할 수 있습니다. 

```
System.out.println(Optional.of("Gyunny").isPresent());      // true 
System.out.println(Optional.ofNullable(null).isPresent());  // false
```

<br>

## `Optional 값 꺼내는 법`

```java
public final class Optional<T> {
    
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    public T orElseThrow() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }
}
```

위와 같이 `get()`, `orElse()`, `orElseGet()`, `orElseThrow()`와 같이 값을 꺼낼 수 있는 API 들이 있습니다. 

사용법을 예제 코드를 보면서 이해해보겠습니다. 

```java
import java.util.Optional;
import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) {
        Optional<String> optional = Optional.of("Gyuuny");
        
        String get = optional.get();          // 1
        System.out.println(get);              // Gyunny

        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Java";
            }
        };

        String orElseGet = optional.orElseGet(supplier);      // 2
        System.out.println(orElseGet);        // Gyunny

        String stringDefault = optional.orElse("default");     // 3
        System.out.println(stringDefault);   // Gyunny
    }
}
```

1. 가장 많이 사용되는 메소드인 get() 입니다. 데이터가 없을 경우에는 널이 return 됩니다. 
2. 람다를 공부하다 보면 나오는 Supplier 함수형 인터페이스를 사용해서 orElseGet() 메소드를 사용할 수 있습니다. 
3. orElse() 메소드는 데이터가 존재하지 않으면 본인이 정한 값을 넣는 메소드 입니다. 

이렇게 Optional은 null처리를 보다 간편하게 하기 위해서 만들어졌습니다. 아래의 내용도 같이 참고해서 보면 좋을 것 같습니다. 

- [Effective Java Item 55](https://github.com/delicious-tangerine/effective-java/blob/master/8%EC%9E%A5/item55.md)
- [자바의 정석 Optional](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Lambda%20%26%20Stream/optional.md)

<br>

## `Default method란?`

자바 8버전 부터는 `default method`와 `static 메소드`가 추가되었습니다. 

```java
public interface Collection<E> extends Iterable<E> {
    
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
}
```

예를들면 `Collection` 인터페이스의 removeIf 라는 default method가 있습니다. 그리고 default method는 abstract method와 달리 하위 클래스에서 반드시 구현하지 않아도 되는 메소드입니다. 

<br>

### `그러면 default method를 왜 만들었을까요?`

바로 `하위 호환성` 때문입니다. 만약에 위와 같이 `Collection` 인터페이스의 하위 클래스들이 사용할 공통적인 메소드인 removeIf가 필요하다고 가정해보겠습니다. 

그런데 이 공통적인 메소드가 abstract 메소드여서 이것을 하위 클래스에서 반드시 구현해야 한다면 수 많은 클래스들의 수정이 이루어져야 할 것입니다. 

이럴 때 `default method`의 기능을 사용하여 메소드를 사용하기 위해서 기능을 만들었습니다. 

<br>

## `병렬 배열 정렬(Parallel array sorting)`

배열을 정렬할 때는 `Arrays.sort()`를 많이 씁니다. 저는 알고리즘을 풀 때 많이 이용을 했었는데요. Arrays 클래스에 자바 8부터 `parallelSort()` 메소드가 추가되었습니다. 

저는 처음보는 메소드인데요.. ㅠ  어떤 역할을 하는 메소드인지 알아보겠습니다. 일단 이름으로 먼저 유추해보면 병렬적으로(동시에) 정렬을 수행을 해주는 그런 메소드라고 추측이 됩니다. 

그래서 이 메소드 내부적으로 정렬을 할 때 자바 7에서 등장한 `Fork-Join 프레임웍`이 사용된다고 합니다. 

```java
public class Arrays {
    public static void parallelSort(byte[] a) {
        int n = a.length, p, g;
        if (n <= MIN_ARRAY_SORT_GRAN ||
            (p = ForkJoinPool.getCommonPoolParallelism()) == 1)
            DualPivotQuicksort.sort(a, 0, n - 1);
        else
            new ArraysParallelSortHelpers.FJByte.Sorter
                (null, a, new byte[n], 0, n, 0,
                 ((g = n / (p << 2)) <= MIN_ARRAY_SORT_GRAN) ?
                 MIN_ARRAY_SORT_GRAN : g).invoke();
    }
}
```

메소드 내부 코드는 위와 같습니다. 내부적으로 `ForkJoinPool` 클래스를 이용하여 정렬을 하는 것을 볼 수 있습니다. 

그러면 실제로 예제코드에서 정렬을 해보겠습니다. 

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] list = {5, 4, 2, 7, 10, 9, 1, 6, 3, 8};

        Arrays.parallelSort(list);
        System.out.println(Arrays.toString(list));  // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    }
}
```

위와 같이 사용하여 정렬을 할 수 있습니다. 

<br>

### `그러면 sort() 메소드와 parallelSort() 메소드 중에 어떤 것을 사용하는게 좋을까요?`

sort()의 경우는 단일 쓰레드로 정렬이 되고, parallelSort()는 필요에 따라 여러 개의 쓰레드로 나뉘어 작업이 수행됩니다. 
(parallelSort()가 CPU를 더 많이 사용할 것입니다.) 책 기준으로 성능 테스트를 해보았을 때 정렬의 개수가 5000개 이하면 큰 차이가 없고 5000개 이상이 되면  
parallelSort()가 더 빨라진다고 합니다. 

물론 이게 엄청 단순하게 말해서 그렇지 [ForkJoinPool](https://github.com/wjdrbs96/Gyunny-Java-Lab/blob/master/Java_God/25%EC%9E%A5/fork%20%26%20join%20%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8D.md) 관련해 계속 정리하면 알아야 할 것이 많은 것 같습니다. 차근차근 정리해보겠습니다. 

