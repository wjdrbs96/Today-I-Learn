# private 생성자나 열거 타입으로 싱글턴임을 보증하라.

`싱글턴(singleton)`이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다. 싱글턴의 전형적인 예로는 함수와 같은 무상태 객체나 설계상 유일해야 하는 시스템 컴포넌트를 들 수 있다. 

<br>

그런데 `클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다` 타입을 인터페이스로 정의한 다음 그 이너페이스를 구현해서 만든 싱클턴이 아니라면
싱글턴 인스턴스를 가짜(mock) 구현으로 대체할 수 없기 때문이다. 

<br>

싱글턴을 만드는 방식은 보통 둘 중 하나다. 두 방식 모두 생성자는 private으로 감춰두고, 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 마련해둔다. 
우선 public static 멤버가 final 필드인 방식을 살펴보자. 

<br>

### 싱글턴을 만드는 첫 번째 방법

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    
    private Elvis() {}
    
    public void leaveTheBuilding() {}
}
```

`private 생성자`는 `public static final` 필드인 Elvis.INSTANCE를 초기화할 때 딱 한 번만 호출된다. public이나 protected 생성자가 없으므로
Elvis 클래스가 초기화될 때 만들어진 인스턴스가 전체 시스템에서 하나뿐임이 보장된다. 위의 코드를 1번이라고 하겠다. 

<br>

### 싱글턴을 만드는 두 번째 방법

싱글턴을 만드는 `두 번째` 방법에서는 정적 팩터리 메소드를 public static 멤버로 제공한다. 

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    public static Elvis getInstance() {
        return INSTANCE;
    }

    public void leaveTheBuilding() {}
}
``` 

Elvis.getInstance는 항상 같은 객체의 참조를 반환하므로 제2의 Elvis 인스턴스란 만들어지지 않는다. 위의 코드를 2번이라고 하겠다.

<br>

### `1번`코드의 `public 필드 방식의 큰 장점`은 

- 해당 클래스가 싱글턴임이 API에 명확히 드러난다는 것이다. public static 필드가 final이니 절대로 다른 객체를 참조할 수 없다.
- `두 번째 장점`은 간결함이다.

### 2번 코드의 정적 팩터리 방식의 장점은

- 마음이 바뀌면 API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다는 점이다. 유이한 인스턴스를 반환하던 팩토리 메소드가 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수 있다.
- 원한다면 정적 팩터리를 제네릭 싱글턴 팩토리로 만들 수 있다는 점이다.

<br>

### 싱글턴을 만드는 세 번째 방법

싱글턴을 만드는 세 번째 방법은 원소가 하나인 열거 타입을 선언하는 것이다. 
 
```java
public enum  Elvis {
    INSTANCE;
    
    public void leaveTheBuilding() {}
}
```

public 필드 방식과 비슷하지만, 더 간결하고 추가 노력 없이 직렬화할 수 있고, 심지어 아주 복잡한 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 완벽히 막아준다.

<br>

조금 부자연스러워 보일 수는 있으나 `대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다` 단, 만들려는 싱글턴이 enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.



