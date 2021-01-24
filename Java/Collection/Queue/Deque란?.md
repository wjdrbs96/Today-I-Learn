# `Deque란 무엇인가?`

```java
public interface Deque<E> extends Queue<E> {

    void addFirst(E e);

    void addLast(E e);

    boolean offerFirst(E e);

    boolean offerLast(E e);

    E removeFirst();

    E removeLast();

    E pollFirst();

    E pollLast();
}
```

`Deque`는 `Queue` 인터페이스를 확장하고 있는 인터페이스입니다. `Queue`는 한쪽 방향에서만 삽입, 삭제를 할 수 있지만, `Deque`는 양쪽 끝에서 삽입, 삭제가 가능합니다. 

![스크린샷 2021-01-18 오후 2 50 27](https://user-images.githubusercontent.com/45676906/104877135-82d99580-599c-11eb-9c56-dd63e67bf510.png)

그래서 인터페이스에 정의된 메소드들을 보면 `addFirst()`, `addLast()`와 같이 앞, 뒤에서 추가할 수 있는 것을 볼 수 있습니다. (삭제도 마찬가지)

<br>

## `예제 코드`

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class Test {
    public static void main(String[] args) {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addFirst(1);
        deque.addLast(2);
        deque.add(1);                     // 맨 뒤에 추가

        for (Integer s: deque) {
            System.out.print(s + " ");   // 1 1 2 1 
        }

        deque.remove();                  // 맨 뒤에 삭제
        deque.removeFirst();

        System.out.println();

        for (Integer s: deque) {
            System.out.print(s + " ");  // 2 1  
        }
    }
}
```

