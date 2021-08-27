# `아이템53 : 가변인수는 신중히 사용하라`

### `인수가 1개 이상이어야 하는 가변인수 메소드의 문제점 코드`

```java
public class Test {
    static int min(int... args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("인수가 1개 이상 필요합니다.");
        }
        
        int min = args[0];
        for (int i = 1; i < args.length; ++i) {
            if (args[i] < min) {
                min = args[i];
            }
        }
        return min;
    }
}
```

- 인수를 0개 넣어 호출하면 런타임에 실패한다는 것이 큰 단점입니다.
- 코드가 지저분합니다.

<br>

### `인수가 1개 이상일 때, 가변인수 제대로 사용하는 법`

```java
public class Test {
    static int min(int firstArg, int... remainingArgs) {
        int min = firstArg;
        for (int arg : remainingArgs) {
            if (arg < min) {
                min = arg;
            }
        }
        return min;
    }
}
```

- 첫 매게변수로 하나 받고, 가변인수로 두 번째로 받으면 위에서 본 문제점이 해결됩니다.

<br>

### `성능에 민감한 상황이라면 가변인수가 걸림돌이 될 수 있다.`

가변인수 메소드는 호출될 때마다 배열을 새로 하나 할당하고 초기화합니다. 

가변인수의 유연성이 필요할 때 선택할 수 있는 패턴이 있습니다. 

예를들어 해당 메소드 호출의 95%가 인수를 3개 이하로 사용한다고 가정하겠습니다. 

그러면 아래처럼 인수가 0개 ~ 4개인 것까지 총 5개를 다중정의 할 수 있습니다. 

```java
public class Test {
    public void foo() {}
    
    public void foo(int a1) {}
    
    public void foo(int a1, int a2) {}
    
    public void foo(int a1, int a2, int a3) {}
    
    public void foo(int a1, int a2, int a3, int...rest) {}
}
```

따라서 5%를 담당하는 가변인수가 있는 메소드만이 배열을 생성합니다. 