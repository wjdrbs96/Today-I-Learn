# `아이템4 : 인스턴스화를 막으려거든 private 생성자를 사용하라`

`정적 메소드`와 `정적 필드`만을 담은 클래스를 만들고 싶을 때가 있을 것입니다. 이것은 객체 지향적을 잘 모르는 사람들이 종종 남용하는 방식이라 좋지는 않지만,
분명 나름의 쓰임새가 있습니다.

예를들면, `java.lng.Math`, `java.util.Arrays`처럼 기본 타입 값이나 배열 관련 메소드들을 모아놓을 수 있습니다. 

```java
public final class Math {

    private Math() {}

    public static final double E = 2.7182818284590452354;

    public static final double PI = 3.14159265358979323846;

    public static double sin(double a) {
        return StrictMath.sin(a);
    }
}
```

```java
public class Arrays {

    private static final int MIN_ARRAY_SORT_GRAN = 1 << 13;
    
    private Arrays() {}

    public static void sort(int[] a) {
        DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
    }
   
}
```

위의 코드는 `Math` 클래스와 `Arrays` 클래스의 일부분인데 보면 `static final` 필드와 `static` 메소드들로만 구성된 것을 볼 수 있습니다. 

<br>

또한 `java.util.Collections`처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메소드(혹은 팩토리)를 모아놓을 수도 있습니다. 

마지막으로 `final` 클래스는 상속이 불가능하기 때문에 이럴 때 필요한 메소드들을 모아놓기도 합니다. 

<br>

## `위와 같이 정적 필드와 정적 메소드를 가진 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 것이 아니다.`

클래스에서 생성자를 명시하지 않으면 컴파일러가 자동으로 public 기본 생성자를 만들어줍니다. 그러면 이 클래스의 인스턴스를 만들 수 있게 됩니다. 

<br>

### `클래스의 인스턴스화를 막고 싶다면 어떻게 해야 할까요?`

`첫 번째`로 추상 클래스를 만드는 방법이 있습니다. 하지만 하위 클래스에서 구현하면 그만이기 때문에 인스턴스화를 막을 수 없습니다. 

다행히도 인스턴스화를 막는 방법은 매우 간단합니다. 직접 생성자를 만들어주면 컴파일러가 public 기본 생성자를 만들어주지 않습니다. 따라서 `private 기본 생성자를 직접 만들어주면 인스턴스화를 막을 수 있습니다.`

위의 `Math` 클래스와 `Arrays` 클래스 모두 생성자의 접근 지정자가 `private`인 것을 볼 수 있습니다. 

생성자의 접근 지정자가 private이니 클래스 밖에서는 접근할 수 없습니다. 

```java
public class Test {
    private Test() {
        throw new AssertionError();
    }
}
``` 

따라서 위와 같이 인스턴스화를 할 때 `AssertionError()`를 던질 필요는 없지만 클래스 안에서 혹시라도 실수로 생성자를 호출하는 경우를 막아줄 수 있습니다. 

그리고 이렇게 생성자를 `private`로 만들면 상속을 불가능하게 합니다. 상속을 했을 때 하위 클래스에서 부모 클래스의 생성자를 호출하게 되는데 부모 클래스 생성자의 접근 지정자가 private 이기 때문에
접근할 수 없기 때문입니다. 


 
 
