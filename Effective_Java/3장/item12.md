# `아이템12 : toString을 항상 재정의하라`

이번 아이템에서는 Object 클래스의 대표적인 메소드인 `toString()`에 대해서 알아보겠습니다. 

```java
public class Object {

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
```

Object의 기본 toString 메소드를 사용하면 위의 메소드가 호출되기 때문에 우리가 만든 클래스에 적절한 문자열을 반환해주지 않습니다. 

```java
public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.toString()); // ExampleCode.Testing.Test@3cb5cdba
    }
}
```

예를들면 위와 같이 `클래스이름@16진수로_표시한_해시코드`를 반환할 뿐입니다.

<br>

## `toString()의 2가지 규약에 대해서 보겠습니다.`

- 간결하고 읽기 쉬운 형태의 유익한 정보를 반환해야 한다.
- 모든 하위 클래스에서 이 메소드를 재정의하라

`equals`와 `hashCode`처럼 엄청 중요한 것은 아니지만 `toString을 잘 구현한 클래스는 디버깅 하기가 훨씬 수월할 것입니다.`

```java
public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test);  // ExampleCode.Testing.Test@3cb5cdba
    }
}
```

위와 같이 println으로 test 객체를 출력해도 toString의 결과가 호출되는 것을 볼 수 있습니다. 이와 같이 toString은 직접 호출하지 않더라도 다른 어딘가에서 쓰이는 경우가 꽤 있습니다.

<br>

## `실전 toString은 그 객체가 가진 주요 정보 모두를 반환하는게 좋다.`

```java
public class PhoneNumber {
    private String number;

    public PhoneNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return number;
    }
}
```
```java
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<String, PhoneNumber> hm = new HashMap<>();

        hm.put("Gyunny", new PhoneNumber("1234-5678"));
        System.out.println(hm.toString()); // {Gyunny=1234-5678}
    }
}
```

예를들어 위와 같이 Map의 toString을 출력했을 때 `{Gyunny=1234-5678}` 이러한 정보가 출력된다면 훨씬 보기가 좋습니다.

<br>

## `toString을 구현할 때면 반환값의 포맷을 문서화할지 정해야 한다.`

- 값 클래스라면 문서화하기를 권한다.
- CSV 같이 데이터 객체 저장도 가능하다.
- 하지만 포맷을 한번 입력하면 영구적으로 해당 포맷에 얽매이게 된다. 
- 포맷 명시 여부와 상관없이 toString()이 반환한 값에 포함된 정보를 얻어올 수 있는 API를 제공하자. 
    - 아마 필드를 하나씩 꺼낼 수 있는 메소드를 만드는 것 같습니다. 
    
<br>

## `toString을 재정의 하지 않아도 되는 경우`

- 유틸리티 클래스
- 대부분의 열거 타입
- 상위 클래스 알맞게 재정의 한 경우

<br>

## `핵심 정리`

> 모든 구현 클래스에서 Object의 toString을 재정의하자. 상위 클래스에서 이미 알맞게 재정의한 경우는 예외다. toString을 재정의한 클래스는 디버깅하기 쉽게 해준다.
> toString은 해당 객체에 관한 명확하고 유용한 정보를 읽기 좋은 형태로 반환해야 한다. 