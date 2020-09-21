## 람다식(Lambda Expression)이란?

람다식(Lambda Expression)은 간단히 말해서 메소드를 하나의 `식(expression)`으로 표헌한 것이다. 람다식은 함수를 간략하면서도 명확한
식으로 표현할 수 있게 해준다. 메소드를 람다식으로 표현하면 메소드의 이름과 반환값이 없어지므로, 람다식을 `익명 함수(annoymous function`
이라고도 한다. 

```java
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int[] arr = new int[5];
        Arrays.setAll(arr, (i) -> (int)(Math.random() * 5) + 1);
    }
}
```

위의 람다식을 보면 메소드를 만들어 사용하는 것보다 훨씬 간단하다. 람다가 아닌 메소드를 사용한다면 메소드가 속해있는
클래스도 만들어야하고, 객체도 새로 생성해야 하기 때문에 번거롭다.

<br>


### 람다식 작성하기

```
public class Test {
    public int max(int a, int b) {
        return a > b ? a : b;
    }
    
    (int a, int b) -> {
        return a > b ? a : b;
    }
}
```

람다식은 `익명 함수`답게 메소드에서 이름과 반환타입을 제거하고 매게변수 선언부와 몸통 { } 사이에 `->`를 추가한다.

<br>

```
    (int a, int b) -> {return a > b ? a : b;}

    (int a, int b) -> a > b ? a : b
```


반환값이 있는 메소드의 경우, return문 대신 `식(expression)`으로 대신 할 수 있다. 식의 연산결과가 자동으로 반환된다. 그리고
이때는 `문장(statement)`이 아닌 `식`이므로 끝에 `;`를 붙이지 않는다.

<br>

```
    (int a, int b) -> a > b ? a : b

    (a, b) -> a > b ? a : b  
```

그리고 람다식에 선언된 매개변수의 타입은 추론이 가능한 경우는 생략할 수 있는데, 대부분의 경우에
생략가능하다. 람다식에 반환타입이 없는 이유도 항상 추론이 가능하기 때문이다.

<br>


```
    (a) -> a * a        =>  a -> a * a          // (OK)

    (int a) -> a * a    => int a -> a * a  // (에러)
```

선언된 매게변수가 하나뿐인 경우에는 괄호()를 생략할 수 있다. 단, 매게변수의 타입이 있으면 괄호()를 생략할 수 없다.
그리고 중괄호의 문장안에 문장이 하나라면 `중괄호 {}`를 생략할 수 있다.


<br>


### 함수형 인터페이스(Functional Interface)

자바에서 모든 메소드는 클래스 내에 포함되어야 하는데, 람다식은 어떤 클래스에 포함되는 것일까? 람다식은 `익명 클래스`의 
객체와 동일하다. `그러면 람다식으로 정의된 익명 객체의 메소드를 어떻게 호출할 수 있을까?` 

```
타임 f = (int a, int b) -> a > b ? a : b  
```

참조변수 f의 타입은 어떤 것이어야 할까? 참조형이니까 클래스 또는 인터페이스가 가능하다. 그리고 람다식과 동등한 메소드가
정의되어 있는 것이야 한다. 그래야 참조변수로 익명 객체(람다식)의 메소드를 호출할 수 있기 때문이다.

<br>

```java
public class Test {
    public static void main(String[] args) {
        MyFunction f = new MyFunction() {
            @Override
            public int max(int a, int b) {
                return a > b ? a : b;
            }
        }; 
        
        // int t = f.max(5, 3)  익명 객체의 메소드 호출
    }
}


interface MyFunction {
    public abstract int max(int a, int b);
}
```

인터페이스를 구현한 익명클래스의 객체를 위와 같이 생성하여 사용할 수 있다. 


<br>

```java
public class Test {
    public static void main(String[] args) {
        MyFunction f = (int a, int b) -> a > b ? a : b;
        int big = f.max(5, 3);
    }
}
```

위와 같이 익명객체를 람다식을 이용하여 대체할 수 있다. 이처럼 `MyFunction`인터페이스를 구현한 익명 객체를 람다식으로 대체가 가능한 이유는,
람다식도 실제로는 익명객체이고, MyFunction인터페이스를 구현한 익명 객체의 메소드 max()와 람다식의 매게변수의 타입과 개수 그리고 반환값이 일치하기 때문이다.

<br>

하나의 메소드가 선언된 인터페이스를 정의해서 람다식을 다루는 것은 기존의 자바의 규칙들을 어기지 않으면서도 자연스럽다.
그래서 인터페이스를 통해 람다식을 다루기로 결정되었으며, 람다식을 다루기 위한 인터페이스를 `함수형 인터페이스(functional interface)`라고 부르기로 했다.

<br>

```java
@FunctionalInterface
public interface Test {  // 함수형 인터페아스 MyFunction을 정의
    public abstract int max(int a, int b);
}
```

`단, 함수형 인터페이스에는 오직 하나의 추상메소드만 정의되어 있어야 한다`는 제약이 있다. 그래야 람다식과 인터페이스의 메소드가 1:1로 연결될 수 있기 때문이다.
반면에 static메소드와 default메소드의 개수에는 제약이 없다.

<br>

```java
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("abc", "aaa", "bbb", "ddd", "aaa");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.compareTo(s1);
            }
        });
    }
}
```

람다식을  사용하지 않는다면 위와 같이 복잡하게 사용해야 했지만, 람다식을 이용하면 아래와 같이 간단하게 할 수 있다.

```java
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("abc", "aaa", "bbb", "ddd", "aaa");
        Collections.sort(list, (s1, s2) -> s2.compareTo(s1));
    }
}
```

<br>

### 함수형 인터페이스 타입의 매게변수와 반환타입

함수형 인터페이스 `MyFunction`이 아래와 같이 정의 되어 있다고 가정하자.


```java
@FunctionalInterface
public interface MyFunction {
    
    void myMethod();  // 추상메소드
}
```

```
void aMethod(MyFunction f) {
    f.myMethod();
}
```

그리고 위와 같이 매게변수 타입이 `MyFunction` 타입이라면, 위의 메소드를 호출할 때 어떻게 해야할까?

<br>

메소드를 호출할 때 람다식을 참조하는 참조변수를 매게변수로 지정해야한다. 예를들면 아래와 같다.

```
MyFunction f = () -> System.out.println("myMethod()");
aMethod(f);
```

또는 참조변수 없이 아래와 같이 직접 람다식을 매게변수로도 넘길 수 있다.

```
aMethod(() -> System.out.println("myMethod()"));  // 람다식을 매게변수로 지정
```

<br>

그리고 메서드의 반환타입이 `함수형 인터페이스`라면 이 함수형 인터페이스의 추상메소드와 동등한 람다식을 가리키는 참조변수를
반환하거나 람다식을 직접 반환할 수 있다.

```
MyFunction myMethod() {
    MyFunction f = () -> {};
    return f;
}
```

람다식을 참조변수로 다룰 수 있다는 것은 메소드를 통해 람다식을 주고받을 수 있따는 것을 의미한다. 즉, 변수처럼 메소드를 주고받는 것이 가능해진 것이다.

<br>

### Example Code

```java
@FunctionalInterface
public interface MyFunction {

    void run();
}

class LambdaEx1 {
    static void execute(MyFunction f) {    // 매게변수가 MyFunction
        f.run();
    }

    static MyFunction getMyFunction() {   // 반환타입이 MyFunction
        MyFunction f = () -> System.out.println("f3.run()");
        return f;
    }

    public static void main(String[] args) {
        MyFunction f1 = () -> System.out.println("f1.run()");

        MyFunction f2 = new MyFunction() {
            @Override
            public void run() {  // public 반드시 붙여야 함
                System.out.println("f2.run()");
            }
        };

        MyFunction f3 = getMyFunction();
        f1.run();
        f2.run();
        f3.run();

        execute(f1);
        execute(() -> System.out.println("run()"));
    }
}
```

```
f1.run()
f2.run()
f3.run()
f1.run()
run()
```

예제 코드를 보면 위에서 정리했던 내용이 모두 나온다. 예를들면 인터페이스를 구현할 때 `익명 클래스`를 사용하지 않고 `람다식`을 이용할 수 있다.
그리고 `반환타입`이 인터페이스 타입, `매게변수 타입`이 인터페이스 타입인 경우를 보여주고 있다.