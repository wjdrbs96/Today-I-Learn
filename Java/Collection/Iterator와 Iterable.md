# `Iterable과 Iterator의 차이`

Collection Framework를 보다 보면 `Iterable`과 `Iterator`이 자주 나옵니다. 이번 글에서는 두 개의 차이에 대해서 알아보겠습니다. 

<br>

## `Iterable 인터페이스`

```java
public interface Collection<E> extends Iterable<E> {
}
```

자바 내부 코드를 보면 `Collection` 인터페이스가 `Iterable` 인터페이스를 extends 하고 있는 것을 볼 수 있습니다. 

![계층](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbE4TfJ%2FbtqBh1w4sLx%2FicJkqcLkLArocYCR4rHUFK%2Fimg.png)

사진으로 보면 위와 같은 계층 구조를 가지고 있습니다. 이번에는 `Iterable` 인터페이스 내부를 한번 보겠습니다. 

```java
public interface Iterable<T> {

    Iterator<T> iterator();

    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
}
```

Iterable 인터페이스의 내부 코드 중 일부를 가져온 것입니다. `Iterator` 인터페이스가 존재하는 것을 볼 수 있습니다. 

<br>

## `Iterator 인터페이스`

그러면 Iterator 인터페이스는 무엇일까요? 

```java
public interface Iterator<E> {

    boolean hasNext();

    E next();
    
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
```

일부 코드를 가져왔는데 핵심적으로 볼 메소드는 `hasNext()`, `next()`가 존재합니다. 

- `hasNext() : 추가 데이터가 있는지 확인하는 메소드`
- `next() : 현재 위치를 다음 요소로 넘기고 그 값을 리턴해주는 메소드`
- `remove() : next()로 읽어 온 욧고를 삭제합니다. next()를 호출한 다음에 remove()를 호출해야 합니다.`

Iterator 인터페이스의 역할은 데이터를 순차적으로 가져올 수 있게 해주는 역할을 합니다. 

> Collection 인터페이스가 Iterable 인터페이스를 extends 한 이유는 하위 클래스에서 iterator()을 반드시 구현하게 하기 위해서 입니다.
>
> List, Set, Queue 인터페이스들 마다 데이터를 꺼내는 방법이 표준화 되어 있지 않다면 데이터를 읽어올 때마다 방법을 제 각각 알아야 하기 때문이 쉽지 않을 것입니다. 
> 그래서 Iterator 인터페이스를 통해서 방법을 표준화 시켜 코드의 일관성을 유지할 수 있습니다.

> 추가로 자바8에 default method가 추가되면서 Iterable 인터페이스에 forEach() 메소드도 추가되었습니다. 

<br>

## `ListIterator 인터페이스`

![ListIterator](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FkJqMb%2FbtqBjVWWCZp%2FCbIAmVoy75UtaoAk2oMbd1%2Fimg.png)

계층 구조를 보면 알 수 있듯이, `Iterator` 인터페이스의 하위 인터페이스 중에 `ListIterator`가 존재하는 것을 볼 수 있습니다. 

```java
public interface ListIterator<E> extends Iterator<E> {

    boolean hasNext();

    E next();

    boolean hasPrevious();

    E previous();

    int nextIndex();

    int previousIndex();

    void remove();

    void set(E e);

    void add(E e);
}
```

위의 메소드를 보면 `previousIndex()`, `nextIndex()`, `previous()`, `hasPrevious()`들이 추가로 있는 것을 볼 수 있습니다. 
Iterator는 `단방향`으로만 이동할 수 있는데 반해 `ListIterator`은 `양방향`으로의 이동이 가능합니다. 
(다만 ArrayList나 LinkedList와 같이 List 인터페이스를 구현한 컬렉션에서만 사용할 수 있습니다.)

<br>

## `부가 정리`

Map 인터페이스가 Collection 인터페이스의 하위 타입이 아닌 이유는 Collection 인터페이스가 Iterable 인터페이스의 하위 인터페이스이기 때문입니다.

Iterable 인터페이스의 주석을 보면 아래와 같이 되어 있습니다. 

```
Implementing this interface allows an object to be the target of
the "for-each loop" statement.
```

즉, List, Set, Queue와 같이 목록형 자료구조는 forEach를 쓸 수 있지만, Map 같이 key-value는 Iterable이 정의한 `object to be the target of the "for-each loop" statement.`
에 맞지 않기 때문에 하위 인터페이스가 아닙니다. 

