# `아이템60 : 정확한 답이 필요하다면 float와 double은 피하라`

float과 double은 과학과 공학 계산용으로 설계되었습니다. 이진 부동 소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 '근사치'로 계산하도록 세심하게 설계되었습니다. 

따라서 정확한 결과가 필요할 때는 사용하면 안 됩니다. 예를들어, float와 double 타입은 `금융 관련 계산`과는 맞지 않습니다.

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(1.03 - 0.42);
    }
}
```

위의 코드의 답은 `0.6100000000000001`가 나옵니다. 

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(1.00 - 9 * 0.10);
    }
}
```

위의 코드는 `0.09999999999999998`가 나오게 되므로 뭔가 계산 결과가 정확하지 않습니다. 

<br>

### 문제: 주머니에는 1달러가 있고, 선반에 10센트, 20센트, 30센트 ... 1달러 짜리의 맛있는 사탕이 놓여 있다고 해보겠습니다. 10센트짜리부터 하나씩 살 수 있을 때 까지 사면 사탕을 몇 개나 살 수 있나 잔돈은 얼마나 남을까요?

```java
public class Test {
    public static void main(String[] args) {
        double funds = 1.00;
        int itemBought = 0;

        for (double price = 0.10; funds >= price; price += 0.10) {
            funds -= price;
            itemBought++;
        }

        System.out.println(itemBought + "개 구입");
        System.out.println("잔돈(달러):" + funds);
    }
}
```

위의 코드에는 문제가 있습니다. 

```
3개 구입
잔돈(달러):0.3999999999999999
```

코드의 결과를 보면 잔돈의 결과가 잘못되었음을 알 수 있습니다. 이를 해결하려면 금융 계산에는 `BigDecimal, int 또는 long을 사용해야 합니다.`

위의 코드의 double 타입을 BigDecimal로 교체하고 실행했습니다. 

```java
import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        final BigDecimal TEN_CENTS = new BigDecimal(".10");

        int itemsBought = 0;
        BigDecimal funds = new BigDecimal("1.00");

        for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >=0; price = price.add(TEN_CENTS)) {
            funds = funds.subtract(price);
            itemsBought++;
        }
        System.out.println(itemsBought + "개 구입");
        System.out.println("잔돈(달러): "  + funds);
    }
}
```
```
4개 구입
잔돈(달러): 0.00
```

그러면 위와 같이 결과가 제대로 나옵니다. 하지만 BigDecimal 에는 단점이 두 가지 있습니다. 

- `기본 타입보다 쓰기가 훨씬 불편하고, 훨씬 느립니다.`

그래서 BigDecimal 대신 int 혹은 long 타입을 쓸 수도 있습니다. `하지만 그럴 경우 다룰 수 있는 값의 크기가 제한되고, 소수점을 직접 관리해야 합니다.`

<br>

## `핵심 정리`

- 정확한 답이 필요한 계산에는 float나 double을 피하라.
- 코딩 시의 불편함이나 성능 저할르 신경 쓰지 않겠다면 BigDecimal을 사용하라.
- 반면에 성능이 중요하고 소수점을 직접 추적할 수 있고 숫자가 너무 크지 않다면 int나 long을 사용하라.


