# `Stack 클래스란 무엇인가?`

Stack 이라는 자료구조는 메모리에서도 쓰이고 실생활에서도 볼 수 있는 자료구조 입니다. 

![stack](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FYhtxB%2FbtqHsbZTFED%2FDhCPI65pmzfsqETjti138k%2Fimg.jpg)

위와 같이 `LIFO(후입 선출)`의 특징을 가지고 있는 것은 다 알고 있을 것입니다. 즉, 한 방향에서만 `삽입`, `삭제`가 일어나는 구조라고 할 수 있습니다. (중간의 데이터를 삭제하고 넣는 것은 불가능합니다.)

<br>

## `Stack 시간복잡도`

- `삽입(Push): 맨 위에 데이터를 넣으면 되기 때문에 O(1) 입니다.`
- `삭제(Pop): 맨 위에 데이터를 삭제하면 되기 때문에 O(1) 입니다.`
- `읽기(Peek): 맨 위의 데이터를 읽으면 되기 때문에 O(1) 입니다.`
- `탐색(Search): 맨 위의 데이터부터 하나씩 찾아야 하기 때문에 O(n)이 걸리게 됩니다.`

<br>

Stack은 위와 같은 특징을 가지고 있습니다. 그러면 자바에서는 어떻게 설계가 되었고 어떤 이슈와 특징을 가지고 있는지 알아보겠습니다. 

<br>

## `Java의 Stack 클래스`

```java
public class Stack<E> extends Vector<E> {

    public Stack() {
    }

    public E push(E item) {
        addElement(item);

        return item;
    }

    public synchronized E pop() {
    }

    public synchronized E peek() {
    }

    public synchronized int search(Object o) {
    }
}
```

Stack 클래스의 내부 코드를 보면 위와 같습니다. 코드를 보면 가장 눈에 띄는 것은 `Vector` 클래스를 확장하고 있는 것과 메소드에 `synchronized`가 붙어있는 것 입니다. 이것이 스택 클래스의 큰 단점인데 구체적으로 어떤 것인지 알아보겠습니다. 

<br>

### `Stack 클래스의 큰 단점 2가지`

1. `Stack 클래스는 synchronized 키워드가 붙어있기 때문에 Thread-safe 하다는 특징을 가지고 있습니다. 즉, 멀티스레드 환경이 아닐 때 사용하면 lock을 거는 작업 때문에 많은 오버헤드가 발생하게 됩니다.`
2. `Stack 클래스는 Vector 클래스를 잘못 확장한 자바의 큰 실수입니다. 왜냐하면 Stack은 LIFO 구조를 이용해야 하기 때문에 Vector 클래스를 확장하면 중간에서 데이터를 삽입, 삭제를 할 수 있게 됩니다.`

```java
import java.util.Stack;

public class Test {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println(stack.get(1));            // 해당 인덱스 원소 찾기
        System.out.println(stack.set(1, 1));         // 해당 인덱스에 원소 넣기
        System.out.println(stack.remove(1));         // 해당 인덱스 원소를 삭제
    }
}
```

Stack은 LIFO 구조로 한쪽 방향에서만 `삽입`, `삭제`가 일어나야 하는데 중간에서 삽입, 삭제를 할 수 있다는 큰 문제가 발생합니다. 이는 Stack, Vector 가 JDK 1.0부터 존재하다 보니 자바에서 잘못 설계를 한 경우라고 합니다.

그리고 마지막으로 큰 단점은 아니지만 `스택은 초기 용량을 설정할 수 있는 생성자가 없다 보니 데이터의 삽입을 많이 하게 되면 배열을 복사해야 하는 일이 빈번하게 발생할 수 있다는` 단점도 존재합니다. 

<br>

## `Stack 클래스 대신 ArrayDeque를 사용하기`

> This class is likely to be faster than Stack when used as a stack, and faster than LinkedList when used as a queue.

ArrayDeque 공식문서에 보면 `스택구조로 사용하면 Stack 클래스보다 빠르고`, `큐 구조로 사용하면 Queue 클래스보다 빠르다`고 합니다.(ArraDeque는 Thread-Safe 하지 않기 때문입니다.)

> Using the Deque interface is the most convenient approach for LIFO data structures as it provides all the needed stack operations

따라서 LIFO 구조를 만들기 위해 적합한 클래스는 Deque 인터페이스를 구현하는 ArrayDeque 클래스입니다.

추가적인 ArrayDeque에 대해 알고 싶다면 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Collection/Queue/ArrayDeque%EB%9E%80%3F.md) 에서 참고하면 됩니다.
