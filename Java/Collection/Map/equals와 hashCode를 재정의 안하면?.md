# `들어가기 전에`

자바에서는 `equals를 재정의하려거든 hashCode도 재정의해라` 라는 말이 있습니다. 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 HashMap이나 HashSet과 같은 컬렉션의 원소로 사용할 때 문제를 일으킨다고 합니다.

왜그럴까요? 먼저 Object 명세에서 발췌한 규약부터 알아보겠습니다. 

<br>

## `Object 명세에서 발췌한 규약`

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메소드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 합니다. 단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없습니다.
- `equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 합니다.`
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없습니다. `단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아집니다.`

<br>

위의 규약을 보면 대략적으로 어떻게 `equals`와 `hashCode`를 재정의해야 할 지 알 수 있습니다.

아래의 예제 코드를 보면서 좀 더 자세히 알아보겠습니다. 

```java
public class PhoneNumber {
    int number1;
    int number2;
    int number3;

    public PhoneNumber(int number1, int number2, int number3) {
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    public static void main(String[] args) {
        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(100, 1001, 1002), "Gyunny");
        System.out.println(m.get(new PhoneNumber(100, 1001, 1002)));
    }
}
``` 

위 코드의 결과는 무엇이 나올까요? 정답은 `null`이 나오게 됩니다. 이유가 무엇일까요? 

위와 같이 `equals()`, `hashCode()`를 재정의하지 않으면 어떤 클래스의 메소드를 사용하게 될까요? 네네~ 맞습니다. Object 클래스를 메소드를 사용하게 됩니다.
 
```java
public class Object {

    public native int hashCode();

    public boolean equals(Object obj) {
        return (this == obj);
    }
}
```

Object 클래스의 메소드는 위와 같습니다. 

- `hashCode()`를 보면 `native`가 붙어있는데 JVM이 내부적으로 메모리 주소를 이용해서 해시코드를 만드는 것 같습니다.
- `equals()` 메소드는 `==` 연산자를 이용합니다. 즉, 주소값이 같으면 true, 다르면 false를 반환하게 됩니다. 

<br>

`HashMap`, `HashSet`에서는 `equals()`, `hashCode()`를 사용하기 때문에 재정의를 하지 않았다면 위와 같은 문제가 발생할 수 있습니다.
물리적으로는 다르지만, 논리적으로 같은 객체임에도 key를 인식하지 못하고 null을 반환하는 문제입니다. 

즉, String pool을 이용한다던지, 같은 메모리 주소를 사용하는 객체만 같은 객체로 인식을 할 것이고 같은 해시코드를 반환하게 될 것입니다. 

<br>

### `equals()는 재정의하고 hashCode()는 재정의하지 않는다면?`

```java
import java.util.HashMap;
import java.util.Map;

public class PhoneNumber {
    int number1;
    int number2;
    int number3;

    public PhoneNumber(int number1, int number2, int number3) {
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return number1 == that.number1 &&
                number2 == that.number2 &&
                number3 == that.number3;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(number1, number2, number3);
//    }

    public static void main(String[] args) {
        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(100, 1001, 1002), "Gyunny");
        System.out.println(m.get(new PhoneNumber(100, 1001, 1002)));
    }
}
```

위의 코드에서 `equals()`는 재정의하고 `hashCode()`는 재정의하지 않았습니다. 이러면 어떻게 될까요? 물리적으로는 다르지만, 논리적으로 같은 객체를 equals() 메소드를 사용하면 true가 나올 것입니다. 

그러나 물리적으로는 다르기 때문에 `hashCode()`는 Object 클래스 메소드를 사용해 다른 값이 나올 것입니다. 즉, 논리적으로 같다고 정의한 객체임에도 불구하고 해시코드 값이 달라 다른 버킷에 저장될 수 있다는 것입니다.

논리적으로 맞지 않는 상황이 생기기 때문에 반드시 `equals()를 재정의하려거든 hashCode()도 재정의하라고 하는 것입니다.`

<br>

## `참고하기`

- [Effective Java Item11](https://devlog-wjdrbs96.tistory.com/255)