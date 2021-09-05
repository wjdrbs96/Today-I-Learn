# `System 클래스란?`

System 클래스는 자바를 처음 배울 때 `System.out.println()` 이라는 출력문을 사용할 때 사용됩니다. 정말 많은 출력문을 사용했지만 System 클래스에 대해서 공부를 해본 적이 없어서.. 이번에 해보려 합니다. 

```java
public final class System {

    private System() {
    }

    public static final InputStream in = null;

    public static final PrintStream out = null;

    public static final PrintStream err = null;
}
```

System 클래스는 위와 같이 `in`, `out`, `err`의 static 변수를 갖고 있습니다. 

- `in : 입력값을 처리할 때 사용한다.`
- `out : 출력값을 처리할 때 사용한다.`
- `err : 에러 및 오류를 출력할 때 사용한다.`

`out`을 보면 선언 및 리턴 타입이 `PrintStream`인 것을 볼 수 있습니다. 그리고 `PrintStream` 클래스에 `println()` 메소드가 존재합니다. 

```
public void println() {
    newLine();
}

public void println(boolean x) {
    synchronized (this) {
        print(x);
        newLine();
    }
}
```

위와 같이 println() 메소드가 오버로딩 되어 구현되어 있습니다. 이러한 이유로 지금까지 `System.out.println()`를 사용할 수 있었던 것입니다.

<br>

## `System 클래스의 역할`

- 시스템 속성(Property)값 관리
- 시스템 환경(Environment)값 조회
- `GC 수행`
- `JVM 종료`
- 현재 시간 조회
- 기타 관리용 메소드들

여기서 절대로 쓰면 안되는 것이 `GC 수행(runFinalization(), gc())`, `JVM 종료(exit(int status))` 관련 메소드입니다. 

그리고 `시스템 속성`, `시스템 환경`에 대한 것들을 관리하고 조회할 수 있는 메소드들이 있지만 따로 정리하지는 않겠습니다. 

<br>

## `현재 시간 조회`

```java
public final class System {

    @HotSpotIntrinsicCandidate
    public static native long currentTimeMillis();

    @HotSpotIntrinsicCandidate
    public static native long nanoTime();
}
```

- `currentTimeMillis() : 현재 시간을 밀리초 단위로 리턴한다.`
- `nanoTime() : 현재 시간을 나노초 단위로 리턴한다.`

위의 메소드를 가지고 시간을 잴 수도 있습니다. (성능 측정할 때 꽤 쓰는 것 같습니다.)

<br>

## `System.out을 살펴보자.`

다른 것보다 `print()`, `println()` 메소드에 대해서 살펴보려 합니다. ln의 차이로 줄바꿈이 일어난다. 안일어난다 정도는 누구나 알고 있을 것입니다. 

그런데 자주 들었던 것은 `System.out.println()`은 `느리다`, `현업에서는 쓰지 않는다`라는 말을 자주 들었습니다. 그런 이유가 무엇일까요?
내부 코드를 보겠습니다. 

```java
public final class System {

    public static final PrintStream out = null;
}
```

static으로 out 변수가 System 클래스 내부에 존재합니다. 이것의 return 타입은 `PrintStream` 입니다. 그리고 PrintStream 클래스 내부에 `print()`, `println()` 메소드가 존재합니다. 

```java
public class PrintStream {
    public void println(int x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }
}
``` 

내부 코드를 보면 `synchronized`가 있는 것을 볼 수 있습니다. 이것은 쓰레드를 공부할 때 보았던 키워드인데요. `동기화`를 시킬 때 쓰는 키워드입니다. 

따라서 많이 메소드가 호출될 때마다 병목 현상이 일어나게 됩니다. 그렇기 때문에 협업에서는 쓰지 않는다고 합니다. 느린 이유에도 한 몫을 하지 않을까 싶습니다. 

<br>

## `착각하기 쉬운 부분`

```java
public class Test {
    private int x;
    private int y;

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test);
    }
}
```
```
Test.Test@2d6e8792
```
 
위의 코드를 실행하면 어떤 것이 출력 될까요? 제가 예전에 보았을 땐 참조변수를 출력하면 내부적으로 `toString()`을 호출한다고 들었습니다. 
(실제로 toString을 오버라이딩 한 후에 출력해서 확인해보면 오버라이딩 한 내용이 출력됩니다.)
그래서 위와 같이 default로 설정되어 있는 값이 찍히는 것 같습니다.

그러면 아래의 코드의 결과는 무엇일까요?

```java
public class Test {
    public static void main(String[] args) {
        Object obj = null;
        System.out.println(obj);
        System.out.println(obj + " Love you");
    }
}
```  
```
null
null Love you
```

에러가 발생하지 않고 위와 같이 결과가 출력됩니다.  음.. 위에서 객체를 찍을 때는 toString()을 호출한다 했는데 `null.toString()`이면 에러가 발생해야 맞는게 아닐까?
라는 생각을 할 수 있습니다.

그런데 `print()`, `println()` 메소드는 단순히 toString() 메소드의 결과를 출력하지 않고 String의 `valueOf()` 메소드를 먼저 호출한 다음에 toString()을 호출하게 됩니다. 

```
String.valueOf(null).toString();
```

위와 같이 내부적으로 일어나게 됩니다. 그래서 에러가 발생하지 않고 결과가 출력될 수 있었던 것입니다. (이거 까지는 몰랐습니다...)

<br>

### `그러면 null과 문자열을 +를 이용해서 합쳤는데 에러가 발생하지 않는 이유는 무엇일까요?`

```
System.out.println(obj + " Love you");
```

위의 코드를 보면 obj는 null이기 때문에 null + Love you가 되는 것입니다. 이것도 실행할 때 컴파일러가 더하기 문장을 `StringBuilder`로 변환합니다. 

```
new StringBuilder().append(obj).append(" is object's value");
```

이러한 이유로 문제 없이 더하기를 할 수 있었던 것입니다. 
