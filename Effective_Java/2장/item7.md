# `아이템7 : 다 쓴 객체 참조를 해제하라`

C, C++에서는 메모리 관리를 직접했을 것이지만, 자바는 GC가 메모리 관리를 해주기 때문에 편리합니다. 하지만 GC가 다 쓴 객체를
알아서 회수해간다고 메모리 관리에 아예 신경을 안쓰면 안됩니다.

```java
import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

언뜻 보면 위의 코드에는 문제가 없어보입니다. 하지만 숨어 있는 문제가 있습니다. 바로 `메모리 누수`입니다.
위의 스택을 오래 사용하다 보면 `가비지 컬렉션` 활동과 `메모리 사용량`이 늘어나 결국 성능이 저하될 것입니다.

<br>

### `메모리 누수는 어디서 된 것일까요?`

스택 코드에서 스택의 pop이 일어나서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않습니다. 이 스택이 그 객체들의 다 쓴 참조(obsolete reference)를 여전히 갖고 있기 때문입니다. 

가비지 컬렉션 언어에서는 의도치 않게 살려두는 메모리 누수를 찾기가 아주 까다롭습니다. 객체 참조 하나를 살려두면 가비지 컬렉터는 그 객체뿐 아니라 그 객체가 참조하는 모든 객체를 회수해가지 못합니다.

위의 pop() 메소드에서 일어나는 메모리 누수를 해결하는 방법은 간단합니다. `해당 참조를 다 썼을 때 null처리(참조 해제)하면 됩니다.`

```java
public class Stack {
    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null;
        return result;
    }
}
```

다 쓴 참조를 null 처리하면 메모리 누수 관리 뿐만 아니라 다른 이점도 있습니다. 
null 처리한 참조를 실수로 사용하려 했을 때 잘못된 객체를 돌려주는 것 보다는 `NullPointerException`을 발생시키는 것이 차리리 더 낫습니다.

그렇다고 필요없는 객체를 볼 때마다 null로 설정하는 코드를 작성하지 않는 것이 좋습니다. `객체 참조를 null 처리하는 일은 예외적인 경우여야 합니다.`

다 쓴 객체를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 `유효 범위(scope)` 밖으로 밀어내는 것입니다. 

```java
public class Stack {
    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        } 

        Integer age = 30;
        Object result = elements[--size];
        elements[size] = null;
        age = null;
        return result;
    }
}
```

예를들어 위와 같이 age 지역 변수가 있을 때 이것을 다 썼다고 바로 null 처리를 하는 것이 아니라, age는 pop() 메소드가 끝나면 사라지기 때문에 null 처리를 따로 하지 않아도 괜찮습니다.
`이렇게 변수의 범위를 최소가 되게 정의했다면 자연스럽게 다 쓴 객체는 메모리 해제가 될 것입니다.`

<br>

### `그러면 null 처리는 언제 해야 할까요?`

`메모리를 직접 관리하는 상황입니다.` 위의 Stack 구현체처럼 elements 배열을 직접 풀을 만들어 관리하고 있습니다. 이럴 때 GC는 어떤 객체가 필요 없는 객체인지 알 수가 없습니다.
그러므로 직접 다 쓴 객체를 null 처리해서 해당 객체를 더 이상 쓰지 않는다고 GC에게 알려주어야 합니다.

<br>

## `캐시 역시 메모리 누수를 일으키는 주범이다.`

```java
import java.util.HashMap;
import java.util.Map;

public class Test1 {
    public static void main(String[] args) {
        Map<Foo, String> map = new HashMap<>();

        Foo key = new Foo();
        map.put(key, "1");

        key = null;
        System.gc();

        map.keySet().forEach(System.out::print); // ExampleCode.Testing.Foo@6e0be858
    }
}
```
 
위와 같이 `HashMap`을 사용해서 캐시를 만들게 되면 key = null로 만들어도 false가 나오는 것을 볼 수 있습니다. 

그러면 `WeakHashMap`을 사용하면 어떻게 될까요?

```java
import java.util.Map;
import java.util.WeakHashMap;

public class Test1 {
    public static void main(String[] args) {
        Map<Foo, String> map = new WeakHashMap<>();

        Foo key = new Foo();
        map.put(key, "1");

        key = null;
        System.gc();

        map.keySet().forEach(System.out::print);
    }
}
```

그러면 위의 결과가 아무 것도 출력이 되지 않습니다. 정리하면 객체의 레퍼런스를 캐시에 넣어 놓고 캐시를 비우는 것을 잊기 쉽습니다. 어려 가지 해결책이 있지만
`캐시의 키`에 대한 레퍼런스가 캐시 밖에서 필요 없어지면 해당 엔트리를 캐시에서 자동으로 비워주는 `WeakHashMap`을 쓸 수 있습니다.

또는 특정 시간이 지나면 캐시값이 의미가 없어지는 경우에 백그라운드 쓰레드를 사용하거나 (ScheduledThreadPoolExecutor 같은) 백그라운드 쓰레드를 활용하거나
캐시에 새 엔트리를 추가할 때 부가적인 작업으로 기존 캐시를 비우는 일을 할 것입니다. (`LinkedHashMap 클래스는 removeEldestEntry 메소드를 제공합니다.`)

<br>

## `메모리 누수의 또 다른 주범은 리스너(listener)와 콜백(callback)이다.`

클라이언트 코드가 콜백을 등록할 수 있는 API를 만들고 콜백을 뺄 수 있는 방법을 제공하지 않는다면, 계속해서 콜백이 쌓일 것입니다. 이것 역시 `WeakHashMap`을 사용해서 해결할 수 있습니다.

<br>

## `핵심 정리`

> 메모리 누수는 겉으로잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다. 이런 누수는 철저한 코드 리뷰나 힙 프로파일러 같은 디버깅 도구를 동원해야만 발견되기도 한다.
> 그래서 이런 종류의 문제는 예방법을 익혀두는것이 매우 중요하다.


 


