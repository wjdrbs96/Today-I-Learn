# `아이템 11: equals를 재정의하려거든 hashCode도 재정의하라`

`equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다`라고 합니다. 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의
인스턴스를 `HashMap`이나 `HashSet`과 같은 컬렉션의 원소로 사용할 때 문제를 일으킨다고 합니다. 

<br>

## `다음은 Object 명세에서 발췌한 규약입니다`

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메소드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 합니다.
단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없습니다. 
- `equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 합니다.` 
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없습니다. `단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아집니다.`

<br>

`hashCode 재정의를 잘못했을 때 크게 문제가 되는 조항은 두 번째입니다. 즉, 논리적으로 같은 객체는 같은 해시코드를 반환해야 합니다.` 
equals 메소드는 두 객체의 물리적 메모리의 위치는 다르지만, 논리적으로는 같게 오버라이딩해서 사용할 수 있습니다. 

<br>

그리고 `Object클래스`에 정의된 `hashCode 메소드`는 객체의 주소값을 이용해서 해시코드를 만들어 반환하기 때문에 서로 다른 두 객체는 절대로 같은 해시코드를 가질 수 없습니다.
그렇기 때문에 `equals` 메소드를 논리적으로 같게 오버라이딩 했다면 반드시 `hashCode` 메소드도 같은 해시코드를 반환하게 오버라이딩 해야합니다.

<br>

### `코드로 예를 한번 들어 볼까요?`

```java
import java.util.Objects;

public class Test {
    public static void main(String[] args) {
        Effective effective1 = new Effective(1, 2);
        Effective effective2 = new Effective(1, 2);
        System.out.println(effective1.hashCode());
        System.out.println(effective2.hashCode());
        
        if (effective1.equals(effective2)) {
            System.out.println("같습니다");
        }
        else {
            System.out.println("다릅니다");
        }
    }
}

class Effective {
    private int a;
    private int b;

    public Effective(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effective effective = (Effective) o;
        return a == effective.a &&
                b == effective.b;
    }
//    @Override
//    public int hashCode() {
//        return Objects.hash(a, b);
//    }
}
```

위의 코드는 `equals` 메소드만 논리적으로 같으면 같다고 오버라이딩을 했고, `hashCode` 메소드는 오버라이딩 하지 않았습니다. 

<br>

### `그러면 코드의 결과가 어떻게 나올까요?`

```
1846274136
1639705018
같습니다
```

위와 같이 나오게 됩니다. `hashCode()` 메소드를 오버라이딩 하지 않았기 때문에 Object 클래스에서 제공하는 메소드가 호출되어 객체의 메모리 값을 이용해서 해시코드를 만들어 반환하기 때문에 값이 다르게 나오는 것을 볼 수 있습니다. 
`equals` 메소드를 통해서  두 객체가 같다고 나오게 되는데 `hashCode`의 값은 다르기 떄문에 살짝 논리적으로 문제가 있다고 생각합니다. 

<br>

그리고 이번에는 위의 `hashCode()` 메소드의 주석을 지우고 위와 같이 오버라이딩 했다고 생각해봅시다. 그러면 객체의 필드 값 a, b를 가지고 해시코드를 만들어 반환하기 때문에 아래와 같이 결과가 나오게 됩니다.

```
994
994
같습니다
```

논리적으로 같은 객체는 해시코드의 값도 같게 나오는 것을 볼 수 있습니다.  

<br>

### `이번에는 책에 있는 예시를 보면서 더 알아보겠습니다.`

```java
import java.util.Objects;

public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix = rangeCheck(prefix, 999, "프리픽스");
        this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
    }
    
    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max) {
            throw new IllegalArgumentException(arg + ": " + val);
        }
        return (short)val;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return areaCode == that.areaCode &&
                prefix == that.prefix &&
                lineNum == that.lineNum;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(areaCode, prefix, lineNum);
//    }
}
```

위의 코드는 10장에서 나온 `PhoneNumber` 클래스입니다. 

<br>

그리고 Main 메소드에서 `Map`을 이용해서 아래와 같이 코드를 작성했습니다.

```java
import java.util.HashMap;
import java.util.Map;

public class TestMap {
    public static void main(String[] args) {
        Map<PhoneNumber, String> m = new HashMap<>();

        m.put(new PhoneNumber(707, 867, 5309), "제니");
        System.out.println(m.get(new PhoneNumber(707, 867, 5309)));
    }
}
```

`위의 코드의 결과는 무엇이 나올까요?` 정답은 `null`이 나옵니다. 이유는 위의 첫 번째로 예시를 들었던 코드를 잘 보았다면 쉽게 예측할 수 있습니다. 

<br>

현재 `PhoneNumber` 클래스에 `hashCode` 메소드를 오버라이딩 하지 않았기 때문에 `new` 연산자를 이용해서 객체를 생성하면 물리적 메모리 위치를 기반으로 해시코드를 만들기 때문에
당연히 논리적으로 같은 객체이더라도 해시코드 값이 다를 것입니다. 따라서 Map의 `key`값이 다르기 때문에 `null`이 출력되는 것입니다. 

<br>

그리고 `HashMap`은 해시코드가 다른 엔트리끼리는 동치성 비교를 시도조차 하지 않도록 최적화 되어 있다고 합니다. 

<br>

### `이 문제는 PhoneNumber 클래스의 hashCode 메서드만 오버라이딩 해주면 해결할 수 있습니다.`

올바른 hashCode 메소드는 어떤 모습이어야 할까요? 우선 먼저 절대로 사용하면 안되는 예시를 들어보겠습니다. 

```java
public class Test2 {
    
    @Override
    public int hashCode() {
        return 42;
    }
}
```

위의 코드는 동치인 객체에게 같은 해시코드를 반환 하기 때문에 적절하다고 생각할 수 있지만, 정말 심각하게도 `모든 객체에게 같은 해시코드의 값을 반환하기 때문에 모든 객체가 해시테이블의 버킷 하나에 담겨 마치 연결리스트처럼 동작하게 됩니다.`

<br>

그러면 평균적인 수행 시간이 `O(1)`인 해시테이블이 위와 같이 같은 버킷에 충돌이 계속 일어난다면 버킷 내에서도 탐색을 해야하기 때문에 `O(n)`으로 느려지게 됩니다. 이와 같이 객체가 많아지면 성능이 매우 나빠져서 사용하기 힘든 수준이 됩니다.

<br>

그리고 해시코드를 오버라이딩 할 때 고려해야 할 것은 `세 번째 규약이 요구하는 '서로 다른 인스턴스에 다른 해시코드를 반환해야 한다'`는 것입니다. 이상적인 해시 함수는 주어진 서로 다른 인스턴스들을 32비트 정수 범위에 균일하게
분배해야 합니다. 

<br>

이상적으로 해시 함수는 만드는 것이 어렵기도 하지만 생각보다 할만하기 때문에 어떻게 좋은 hashCode를 작성하는지 알아보겠습니다.

<br>

Effective Java 책에서는 아래와 같이 `좋은 hashCode를 만드는 방법`에 대해서 나열하고 있습니다.


## `좋은 hashCode를 만드는 방법`

1) int 변수 result를 선언한 후 값 c로 초기화한다. 이때 `c는 해당 객체의 첫 번째 핵심 필드 단계 2.a 방식으로 계산한 해시코드입니다.` (여기서 핵심필드란 equals 비교에 사용되는 필드를 뜻합니다.)
2) 해당 객체의 나머지 핵심 필드 f 각각에 대해 다음 작업을 수행합니다.  
    - a. 해당 필드의 해시코드 c를 계산합니다.
        - i. 기본 타입 필드라면, Type.hashCode(f)를 수행합니다. 여기서 Type은 해당 기본 타입의 박싱 클래스입니다.(int라면 Integer가 Type이 됩니다.)
        - ii. 참조 타입 필드면서 이 클래스의 equals 메소드가 이 필드의 equals를 재귀적으로 호출해 비교한다면, 이 필드의 hashCode를 재귀적으로 호출합니다.
        계산이 복잡해질 것 같으면, 이 필드의 표준형을 만들어 그 표준형의 hashCode를 호출합니다. 필드의 값이 null이면 0을 사용합니다.
        - iii). 필드가 배열이라면, 핵심 원소 각각을 별도 필드처럼 다룹니다. 이상의 규칙을 재귀적으로 적용해 각 핵심 원소의 해시코드를 계산한 다음, 단계 2.b방식으로 갱신합니다. 
        배열의 핵심 원소가 하나도 없다면 단순히 상수(0를 추천합니다.)를 사용합니다. 모든 원소가 핵심 원소라면 Arrays.hashCode를 사용합니다.
    - b. 단계 2.a에서 계산한 해시코드 c로 result를 갱신한다. 코드로는 다음과 같다.
        - result = 31 * result + c;
3) result를 반환한다.

<br>

위의 말만 보았을 때는 아직 자세히 이해가 되지 않는 것들이 꽤 있는 것 같습니다. 아래의 내용을 보면서 좀 더 자세히 알아보겠습니다.


### `위에서 말한 것처럼 hashCode를 다 구현했다면 이 메소드가 동치인 인스턴스에 대해 똑같은 해시코드를 반환할지 검증해보아야 합니다.`

만약 같은 객체임에도 다른 해시코드를 반환한다면 원인을 찾아보아야 합니다. 본인이 해시코드를 아래의 보기들을 잘 생각하면서 구현했는지 보면서 원인을 찾아보면 좋을 것 같습니다. 

- 다른 클래스로부터 파생된 필드는 해시코드 계산에서 제외해도 됩니다. 
- 또한 equals 비교에 사용되지 않은 필드는 해시 코드를 계산할 때 `반드시!!` 제외해야 합니다. (그렇지 않으면 hashCode 규약 두 번째를 어기게 될 위험이 있습니다.)
- `그리고 2.b의 곱셈 31 * result는 필드를 곱하는 순서에 따라 result 값이 달라지게 합니다.` 이렇게 곱셈을 이용하지 않고 덧셈만을 이용해서 해시코드를 구한다면 문자열에서 모든 `아나그램(anagram, 구성하는 철자가 같고 그 순서만 다른 문자열)`의 해시코드가 같아지게 됩니다.
(덧셈은 순서가 바뀌어도 값이 같지만 위와 같은 로직으로 곱셈을 적용하면 값이 달라지게 되기 때문이다.)
- 곱하는 숫자를 31로 정한 이유는 31이 홀수이면서 소수(prime)이기 때문입니다. 만약에 이 숫자가 짝수이고 오버플로가 발생한다면 정보를 잃게 됩니다. 이유는 2를 곱하는 것은 시프트 연산과 같은 결과를 내기 때문입니다.

<br>

이러한 `hashCode`를 만드는 팁을 이용해서 PhoneNumber 클래스에 적용해봅시다. 

```java
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    @Override
    public int hashCode() {
        int result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        return result;
    }
}
```

클래스에 다른 메소드와 생성자를 제외하고 `필드`, `hashCode()`만 코드로 나타내었습니다. 
코드를 보면 위에서 해시 코드를 만드는 방법의 2.a.i에서 설명한 것처럼 `기본 타입인 short`를 `박싱 클래스인 Short를 사용하여 hashCode를 구하는 것을 볼 수 있습니다.`


<br>

`hashCode()` 메소드는 핵심 필드인 `areaCode`, `prefix`, `lineNum` 3개를 사용해 간단한 계산을 하게 됩니다.
위에서 정의한 hashCode 메소드는 자바 플랫폼 라이브러리의 클래스들이 제공하는 hashCode 메소드와 비교해도 부족함이 없습니다. 그만큼 단순하고 빠르고 충돌이 일어날 가능성도 적고 훌륭하다고 생각합니다. 


<br>

그리고 `Objects 클래스`는 임의의 개수만큼 객체를 받아 해시코드를 계산해주는 정적 메소드인 hash를 제공합니다. 

```java
public final class Objects {

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }
}
```

위의 코드는 `Objects` 클래스의 내부 코드에서 `equals()`, `hash()`, `hashCode()` 메소드만 가져왔습니다. 여기서 `hashCode()` 메소드를 사용하면
`PhoneNumber` 클래스에서 hashCode를 정의했던 것을 단 한 줄로 작성할 수 있습니다.

<br>

하지만 코드가 더 짧지만 사용하지 않는 이유는 `속도가 더 느리기 때문입니다.` 입력 인수를 담기 위한 배열이 만들어지고(`Arrays.hashCode(values)`), 입력 중 기본 타입이 있다면 박싱과 언박싱도 거치기 때문입니다.
그러니 hash 메소드는 성능에 민감하지 않은 상황에서만 사용하는 것을 권장합니다. 

```java
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    @Override
    public int hashCode() {
        return Objects.hash(lineNum, prefix, areaCode);
    }
}
```

위의 코드와 같이 `Objects.hash()`를 사용하면 한 줄로 hashCode() 메소드를 만들 수 있습니다. 

<br>

### `만약 클래스가 불변이고 해시코드를 계산하는 비용이 크다면, 매번 새로 계산하기 보다는 캐싱하는 방식을 고려해야 합니다.`

- 이 타입의 객체가 주로 해시의 키로 사용될 것 같다면 인스턴스가 만들어질 때 해시코드를 계산해둬야 합니다. 
- 해시의 키로 사용되지 않는 경우라면 hashCode가 처음 불릴 때 계산하는 `지연 초기화(lazy initialization) 전략`이 있습니다. 필드를 지연 초기화하려면 그 클래스를 스레드 안전하게 만들도록 신경 써야 합니다. 이것은 아이템 83에서 다시 다루겠습니다.

```java
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;
    private int hashCode;

    @Override
    public int hashCode() {
        int result = hashCode;  // 자동으로 0으로 초기화 된다.
        
        if (result == 0) {
            result = Short.hashCode(areaCode);
            result = 31 * result + Short.hashCode(prefix);
            result = 31 * result + Short.hashCode(lineNum);
            hashCode = result;
        }
        return result;
    }
}
```

위의 코드처럼 `지연 초기화` 방식을 이용해서 사용할 수 있습니다. (대신 스레드의 안정성을 신경써야 합니다.) 그리고 `성능을 높인답시고 해시코드를 계산할 때 핵심 필드를 생략해서는 안 됩니다.`
속도야 빨라질 수 있지만, 해시 품질이 나빠져 해시 테이블의 성능이 엄청나게 나빠질 수도 있습니다.

<br>  
 
예를들면, 특히 어떤 필드는 특정 영역에 몰린 인스턴스들의 해시코드를 넓은 범위로 고르게 퍼트려주는 효과가 있을지도 모릅니다.
하필 이런 필드를 생략한다면 해당 영역의 수많은 인스턴스가 단 몇 개의 해시코드로 집중되어 해시테이블의 속도가 선형으로 느려질 것입니다.

<br>

위에서 말한 예시가 단순히 `너무 이론적인거 아닌가?` 라고 할 수도 있지만 실제로 자바 2 전의 String은 최대 16개의 문자만으로 해시코드를 계산했습니다.
문자열이 길면 균일하게 나눠 16 문자만 뽑아내 사용한 것입니다. URL처럼 계층적인 이름을 대량으로 사용한다면 이런 해시 함수는 앞서 이야기한 심각한 문제를 고스란히 드러낼 수 있습니다.

<br>

## `핵심 정리`

> equals를 재정의할 때는 hashCode도 반드시 재정의해야 한다. 그렇지 않으면 프로그램이 제대로 동작하지 않을 것이다. 재정의한 hashCode는 Object의 API 문서에 기술된 일반 규약을 따라야 하며,
> 서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해야 한다. 

