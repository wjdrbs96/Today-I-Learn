# 다 쓴 객체 참조를 해제하라

C, C++처럼 메모리를 직접 관리해야 하는 언어를 쓰다가 자바처럼 가비지 컬렉터를 갖춘 언어로 넘어오면 코딩하기가 훨씬 수월해진다. 
다 쓴 객체를 알아서 회수해가니 말이다. 처음 자바르 해볼 때는 매우 신기할 수 있고, 자칫 메모리 관리에 더 이상 신경 쓰지 않아도 된다고 오해할 수 있다.
`하지만 절대로 그렇지 않다.`

<br>

### 다음은 `스택`을 구현한 다음 코드를 보자.

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

위의 코드에서 특별한 문제는 없어 보인다. 어떤 테스트를 해도 잘 작동할 것이다. 하지만 숨어있는 문제는 바로 `메모리 누수`이다. 
이 스택을 사용하는 프로그램을 오래 실행하다 보면 점차 가비지 컬렉션 활동과 메모리 사용량이 늘어나 결국 성능이 저하될 것이다. 

<br>

상대적으로 드문 경우긴 하지만 심할 때는 `디스크 페이징`이나 `OutofMemoryError`를 일으켜 프로그램이 예기치 않게 종료되기도 한다. 

<br>

### 위의 코드에서 `메모리 누수`는 어디에서 일어날까? 

이 코드에서는 스택이 커졌다가 줄어들었을 때 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않는다. 프로그램에서 그 객체들을 더 이상 사용하지 않더라도 말이다. 

<br>

이 스택이 그 객체들의 다 쓴 참조(obsolete reference)를 여전히 가지고 있기 때문이다. 여기서 다 쓴 참조란 문자 그대로 앞으로 다시 쓰지 않을 참조를 뜻한다. 

<br>

위의 코드에서는 elements 배열의 `활성 영역` 밖의 참조들이 모두 여기에 해당한다. 가비지 컬렉터 언어에서는 의도치 않게 객체를 살려두는 `메모리 누수`를 찾기가 아주 까다롭다. 

<br>

객체 참조 하나를 살려두면 가비지 컬렉터는 그 객체뿐 아니라 그 객체가 참조하는 모든 객체(그리고 또 그 객체들이 참조하는 모든 객체 ...)를 회수해가지 못한다. 

<br>

그래서 단 몇개의 객체가 매우 많은 객체를 회수되지 못하게 할 수 있고 잠재적으로 성능에 악영향을 줄 수 있다. 

<br>

### 이러한 문제의 해법은 다 쓴 객체를 null로 처리하자.  

```java
import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        Object result = elements[--size];
        elements[size] = null;         // 다 쓴 참조 해제
        return result;
    }
}
```

위에는 pop 메소드를 수정한 코드이다. 위와 같이 다 쓴 참조를 null 처리하면 다른 이점도 따라온다. 만약 null 처리한 참조를 실수로 사용하려 하면 프로그램은 즉시
`NullPointerException`을 던지며 종료된다. 

<br>

## 그렇다면 null 처리는 언제해야 할까? Stack 클래스는 왜 메모리 누수에 취약한 걸까?

바로 스택이 `자기 메모리`를 직접 관리하기 때문이다. 이 스택은 객체 자체가 아니라 객체 참조를 담는 elements 배열로 저장소 풀을 만들어 원소들을 관리한다. 

<br>

배열의 활성 영역에 속한 원소들이 사용되고 비활성 영역은 쓰이지 않는다. 가비지 컬렉터는 이 사실을 알 수가 없기 때문에 객체가 쓸모없다는 것을 알리기 위해 
`null`처리를 해서 해당 객체를 더는 쓰지 않을 것임을 명시해야 한다. 

<br>

일반적으로 `자기 메모리를 직접 관리하는 클래스라면 프로그래머는 항시 메모리 누수에 주의해야 한다.` 원소를 다 사용한 즉시 그 원소가 참조한 객체들을 다 null 처리해줘야 한다.

