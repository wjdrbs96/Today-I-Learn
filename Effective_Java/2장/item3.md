# `아이템3 : private 생성자나 열거 타입으로 싱글턴임을 보증하라`

## `싱글턴(singleton)이란?`

`인스턴스를 오직 하나만 생성할 수 있는 클래스를 말합니다.` 대표적인 예로는 스프링의 의존성 주입이 생각이 납니다. 
그리고 함수와 같은 무상태(stateless) 객체나 설계상 유일해야 하는 시스템 컴포넌트를 예로 들 수 있습니다. 

<br>

## `싱글턴의 단점`

- 하지만 이렇게 클래스를 싱글톤으로 만들게 되면 `이를 사용하는 클라이언트를 테스트하기가 어려워`질 수 있습니다. 
- 왜냐하면 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해서 만든 싱글턴이 아니라면 싱글턴 인스턴스를 가짜(mock) 구현으로 대체할 수 없기 때문입니다.

<br>

## `싱글턴을 만드는 방법`

- 생성자는 private로 감춰두고, 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 마련해둡니다.
- 생성자는 private로 감춰두고, 정적 팩토리 메소드를 public static 멤버로 제공합니다.
- 원소가 하나인 열거 타입을 선언하는 것입니다. 

<br>

### `방법1 : public static final 필드 방식의 싱글턴`

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    public void leaveTheBuilding() {}
}
```

`priavte 생성자는 public static final 필드인 ELvis.INSTANCE`의 객체를 만들 때 한번 호출 됩니다. public이나 protected 생성자가 없기 때문에 Elvis 클래스가 초기화될 때
만들어진 인스턴스가 하나뿐임을 보장할 수 있습니다. 

<br>

### 장점

- 위의 public 필드 방식의 큰 `장점`은 해당 클래스가 싱글턴임이 API에 명백히 드러난다는 것입니다.
- 그리고 간결하다는 장점이 있습니다. 

<br> 


그런데 위의 코드의 예외가 한 가지가 있는데, 권한이 있는 클라이언트는 `리플렉션 API`인 `AccessibleObject.setAccessible`을 사용해 private 생성자를 호출할 수 있다는 것입니다. 이러한 예외에는
생성자를 수정하여 두 번째 객체가 생성되려 할 때 예외를 던지게 하면 됩니다. 

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private static int count = 0;
    
    private Elvis() {
        count++;
        if (count > 1) {
            throw new IllegalStateException("This Object should be singleton");
        }
    }

    public void leaveTheBuilding() {}
}
```

<br>

### `방법2 : 정적 팩터리 메소드를 public static 멤버로 제공`

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

클리이언트가 `getInstance()` 메소드를 통해서 Elvis 객체를 반환 받아도 항상 같은 객체를 반환 받게 됩니다. 

<br>

### 장점

- API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다는 점입니다. 
    - 가령 API라 하면 `getInstance()` 메소드를 의미하는데 `return new INSTANCE`라고 내부 코드를 바꾸게 되면 싱글톤이 아니게 바꿀 수 있습니다.
- 유일한 인스턴스를 반환하던 팩토리 메소드가 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수 있습니다. 
- 원한다면 정적 팩토리를 제네릭 싱글턴 팩토리로 만들 수 있다는 점입니다. 
- 정적 팩토리의 메소드 참조를 공급자로 사용할 수 있다는 점입니다. 

```java
import java.util.function.Supplier;

public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() {
        return INSTANCE;
    }

    public void leaveTheBuilding() {}

    public static void main(String[] args) {
        Elvis elvis = Elvis.getInstance();
        Supplier<Elvis> supplier = Elvis::getInstance;
    }
}
```

<br>

> 직렬화의 정리 필요합니다  ----------------------------------

<br>

### `방법3 : 원소가 하나인 열거 타입을 선언`

```java
public enum Elvis {
    INSTANCE;
    
    public void leaveTheBuilding() {  }
}
```

public 필드 방식과 비슷하지만 더 간결하고, 추가 노력 없이 직력화할 수 있고, 아주 복잡한 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 완벽히 막아줍니다. 
살짝 부자연스러워 보일 수는 있지만 `대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법입니다.`
