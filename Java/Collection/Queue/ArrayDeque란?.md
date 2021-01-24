# `들어가기 전에`

이번 글에서는 `ArrayDeque` 클래스에 대해 알아보겠습니다. ArrayDeque 클래스는 이름에서도 알 수 있듯이 Deque와 관련된 클래스인 것을 알 수 있습니다.

그리고 Stack 클래스의 문제점, LIFO 구조를 만들 때 ArrayDeque로 해야 하는 상황에 대해서 정리해보겠습니다. 

```java
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, Serializable {}
```

그래서 상속관계를 보니 `Deque` 인터페이스를 구현하고 있는 클래스인 것을 알 수 있습니다. 

그런데 `Stack 클래스 대신에 ArrayDeque 클래스를 사용해서 LIFO 구조를 만들어라` 라는 말이 있습니다. 

먼저 스택 클래스의 문제점에 대해서 살펴보겠습니다.

<br>

## `Stack 클래스의 문제점 2가지`

```java
public class Stack<E> extends Vector<E> {

    public Stack() {
    }

    public E push(E item) {
        addElement(item);

        return item;
    }

    public synchronized E pop() {
        E       obj;
        int     len = size();

        obj = peek();
        removeElementAt(len - 1);

        return obj;
    }

    public synchronized E peek() {
        int     len = size();

        if (len == 0)
            throw new EmptyStackException();
        return elementAt(len - 1);
    }

    public boolean empty() {
        return size() == 0;
    }

    public synchronized int search(Object o) {
        int i = lastIndexOf(o);

        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }

}
```

Stack 클래스는 Vector 클래스를 확장하고 있는 것을 볼 수 있습니다. 그리고 메소드들 마다 `synchronized` 키워드를 붙혀 멀티 스레드 환경에서 `Thread-Safe`한 것을 볼 수 있습니다. 
(push 메소드도 내부 코드를 따라가다 보면 Vector 클래스의 push를 사용해 synchronized가 되어있습니다.)

Stack 클래스의 단점 2가지를 정리하면 아래와 같습니다. 

- `Stack 클래스를 만들 때 초기 용량을 설정할 수 없다.`
- `모든 메소드에 synchronized가 있기 때문에 단일 스레스 환경에서는 성능이 떨어진다.`

<br>

이러한 단점을 보완하기 위해서 LIFO 구조를 만들 때 `ArrayDeque`를 사용해라 라고 합니다. 

<br>

## `ArrayDeque는 무엇일까?`

> This class is likely to be faster than Stack when used as a stack, and faster than LinkedList when used as a queue.

ArrayDeque 공식문서에 보면 `스택구조로 사용하면 Stack 클래스보다 빠르고`, `큐 구조로 사용하면 Queue 클래스보다 빠르다`고 합니다.

> Using the Deque interface is the most convenient approach for LIFO data structures as it provides all the needed stack operations

이처럼 LIFO 구조를 만들기 위해 적합한 클래스는 Deque 인터페이스를 구현하는 ArrayDeque 클래스입니다.   

하지만 `ArrayDeque`는 `Thread-Safe`하지 않다는 단점이 있습니다. 그래서 `멀티 쓰레드` 환경에서는 문제가 있습니다. 그래서 아래와 같이
synchronized를 장식해 ArrayDeque를 만들 수 있습니다.

```java
public class DequeBasedSynchronizedStack<T> {

    // Internal Deque which gets decorated for synchronization.
    private ArrayDeque<T> dequeStore;

    public DequeBasedSynchronizedStack(int initialCapacity) {
        this.dequeStore = new ArrayDeque<>(initialCapacity);
    }

    public DequeBasedSynchronizedStack() {
        dequeStore = new ArrayDeque<>();
    }

    public synchronized T pop() {
        return this.dequeStore.pop();
    }

    public synchronized void push(T element) {
        this.dequeStore.push(element);
    }

    public synchronized T peek() {
        return this.dequeStore.peek();
    }

    public synchronized int size() {
        return this.dequeStore.size();
    }
}
```

<br>

## `ArrayDeque 다른 특징들`

> Resizable-array implementation of the Deque interface. Array deques have no capacity restrictions

공식 문서에 보면 위와 같이 ArrayDeque는 용량 제한이 없다고 말합니다. 내부적으로 어떤 방식으로 일어나는지 알아보겠습니다. 

```java
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, Serializable
{
    transient Object[] elements; // non-private to simplify nested class access

    transient int head;

    transient int tail;

    public ArrayDeque() {
        elements = new Object[16];
    }

    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p; // number of elements to the right of p
        int newCapacity = n << 1;
        if (newCapacity < 0)
            throw new IllegalStateException("Sorry, deque too big");
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }  
}
```

위의 코드는 ArrayDeque 내부 코드입니다. 일단 먼저 생성자에 매개변수 없이 만들면 `기본 용량이 16`인 것을 볼 수 있습니다. 그러면 공식문서에서 나온거 처럼 ArrayDeque는 기본 용량을 넘으면 어떻게 용량을
추가할까요?

그건 `doubleCapacity()` 메소드를 보면 알 수 있습니다. 

- `int n = elements.length;`
- `int newCapacity = n << 1;`

위와 같이 배열의 길이의 2배 만큼 늘리는 것을 볼 수 있습니다. 그 외적인 `Stack`, `Queue` 구조로 할 수 있는 메소드들이 있습니다. 

<br>

# `Reference`

- [https://www.baeldung.com/java-lifo-thread-safe](https://www.baeldung.com/java-lifo-thread-safe)