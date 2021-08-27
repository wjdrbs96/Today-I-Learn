# `아이템34 : int 상수 대신 열거 타입을 사용하라`

```java
public class Test {
    public static final int APPLE = 0;
    public static final int ORANGE = 0;
}
```

위와 같이 정수 타입의 상수를 사용하면 문제가 많기 때문에 `enum`을 이용하자.

- 열거 타입은 인스턴스 통제된다. 
- 열거 타입은 컴파일타임 타입 안전성을 제공한다. 
- 열거 타입은 임의의 메소드나 필드를 추가할 수 있고 임의의 인터페이스를 구현하게 할 수도 있다. 
- 열거타입은 근본적으로 불변이라 모든 필드는 final이어야 한다. 
- 상수가 더 다양한 기능을 제공해줬으면 할 때
    - 열거타입은 상수별로 다르게 동작하는 코드를 구현하는 더 나은 수단을 제공한다. 이를 `상수별 메소드 구현`이라 한다.
    
```java
public enum Operation {
    PLUS {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    };
    
    public abstract double apply(double x, double y);
}
```

<br>

## 핵심 정리

> 열거 타입은 확실히 정수 상수보다 뛰어나다. 더 읽기 쉽고 안전하고 강력하다. 대다수 열거 타입이 명시적 생성자나 메소드 없이 쓰이지만,
> 각 상수를 특정 데이터와 연결짓거나 상수마다 다르게 동작하게 할 때는 필요하다. 드물게는 하나의 메소드가 상수별로 다르게 동작해야 할 때도 있다.
> 이런 열거 타입에서는 switch 문 대신 상수별 메소드 구현을 사용하자. 



