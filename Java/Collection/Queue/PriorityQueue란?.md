# `PriorityQueue란?`

자바에는 `PriorityQueue` 클래스가 존재합니다.

```java
public class PriorityQueue<E> extends AbstractQueue<E>
    implements java.io.Serializable {}
```

위와 같이 `AbstractQueue`와 `Queue` 인터페이스를 구현한 클래스 중에 하나입니다. 즉, `PriorityQueue`는 `힙(Heap)`을 구현하고 있는 클래스입니다. 

먼저 `힙(Heap)`이란 무엇인지 알아보고 가겠습니다. 

<br>

## `힙(Heap)이란?`
 
- `완전 이진 트리의 일종으로 우선순위 큐를 위하여 만들어진 자료구조 입니다.ex) OS 작업 스케쥴링`
- `여러 개의 값들 중에서 최대값이나 최소값을 빠르게 찾아내도록 만들어진 자료구조 입니다.`
- `중복된 값을 허용합니다.`
- `힙은 느슨한 정렬 상태를 유지한다. (완전히 정렬된 것은 아니지만, 전혀 정렬이 안된 것도 아님)`
- `힙의 목적은 삭제 연산이 수행될 때마다 가장 큰 값을 찾아내기만 하면 되는 것이다(가장 큰 값은 루트 노드)`

<br>

## `힙(Heap)의 종류`

![heap](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCRFEy%2FbtqAQ4f5oow%2FtukgTGyHgLxmXLptx5mNEk%2Fimg.png)

힙에는 `최대 힙(max heap), 최소 힙(min heap)`이 존재합니다. 

- `최대 힙은 루트 노드에 가장 큰 값을 가지고 있습니다.` 
- `최소 힙은 루트 노드에 가장 작은 값을 가지고 있습니다.`

<br>

<br>

## `힙(Heap) 삽입 연산`

- `힙의 새로운 요소가 들어오면, 일단 새로운 노드를 힙의 마지막 노드로 삽입합니다.`
- `삽입 후에 새로운 노드를 부모 노드들과 힙의 조건에 맞게 교환해주면 됩니다.`

![heap](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FnJZPN%2FbtqASy8B59k%2F8S0hi1P1HRKanEKbVDYB9k%2Fimg.png)

즉, `최대 힙`의 구조라면 위와 같은 방식으로 자리를 바꿔주면 됩니다. 

<br>

## `힙(Heap) 삭제 연산`

- `최대 힙에서 삭제연산은 최대값을 가진 요소를 삭제하는 것입니다.`
- `최대 힙에서 최대 값은 루트 노드이므로 루트 노드를 삭제합니다.`

![delete](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCYgd0%2FbtqBg8gU4WA%2FNXtuOb0W2x2ZSj42TKKqE0%2Fimg.png)

<br>

## `Java에서 최소 힙(Heap) 사용하기`

```java
import java.util.PriorityQueue;

public class Test {
    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();

        priorityQueue.add(1);
        priorityQueue.add(2);
        priorityQueue.add(3);

        priorityQueue.forEach(System.out::println);
    }
}
```
```
1
2
3
```

그리고 `최대 힙`으로 사용하려면 아래와 같이 사용하면 됩니다. 

```java
public class Test {
    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());
    }
}
```


`PriorityQueue` 클래스가 Heap을 구현하고 있는 클래스입니다. 기본 생성자를 사용했을 때는 `최소 힙`으로 구현됩니다. 

<br>

### `Java PriorityQueue 클래스 특징`

- `null 값을 허용하지 않습니다.`
- `비교할 수 없는 객체를 허용하지 않습니다.`
- `Thread-safe 하지 않습니다.`
    - `멀티스레드 환경에서는 PriorityBlockingQueue를 사용하면 됩니다.`
- `iterator() 메소드로 순회했을 때 순서를 보장하지 않습니다.`
- `DEFAULT_INITIAL_CAPACITY = 11 입니다.(기본 초기 용량)`

![스크린샷 2021-01-25 오후 1 45 21](https://user-images.githubusercontent.com/45676906/105662716-c5a6ea80-5f13-11eb-8079-dc964abd619b.png)

용량이 초과하면 위와 같이 `자동으로` 용량을 늘려주는 것을 볼 수 있습니다. 

- `원래 용량이 64보다 작으면 원래용량 + 원래용량 + 2로 늘려줍니다. 즉, 거의 2배로 늘려줍니다.`
- `원래 용량이 64보다 크면 원래용량 + 원래용량/2로 늘려줍니다. 즉, 1.5배로 늘려줍니다.`

<br>

## `힙(Heap)의 시간 복잡도`

|add(), offer()|remove()|remove(Object), contains()|peek(), size()|
|-------|-----|-----|------|
|O(logN)|O(logN)|O(n)|O(1)|
