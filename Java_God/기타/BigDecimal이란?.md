# `BigDecimal이란?`

BigDecimal 클래스는 `돈 계산`과 같이 중요한 작업을 할 때 사용합니다. float, double은 사용하면 안될까요?

```java
public class Test {
    public static void main(String[] args) {
        double value = 1.0;
        for (int i = 0; i < 10; ++i) {
            value += 0.1;
            System.out.println(value);
        }
    }
}
``` 

위의 코드를 보겠습니다. 결과는 무엇이 나올까요? 2.0이 나올까요?

```
1.1
1.2000000000000002
1.3000000000000003
1.4000000000000004
1.5000000000000004
1.6000000000000005
1.7000000000000006
1.8000000000000007
1.9000000000000008
2.000000000000001
```

위와 같이 결과가 나오게 됩니다. 이처럼 float, double은 근사치를 제공할 뿐이지, 정확한 값을 제공해주지 못합니다.(그 이유는 [여기](https://github.com/wjdrbs96/Today-I-Learn/blob/master/Java/Data-Type/float%20vs%20double%20%EC%B0%A8%EC%9D%B4%EB%8A%94%3F.md) 에서 확인하면 됩니다.)

따라서 돈 계산같이 정확한 계산을 할 때에는 `BigDecimal`을 사용해야 합니다. 

```java
import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        BigDecimal value = new BigDecimal("1.0");
        BigDecimal addValue = new BigDecimal("0.1");
        for (int i = 0; i < 10; ++i) {
            value = value.add(addValue);
            System.out.println(value.toString());
        }
    }
}
```
```
1.1
1.2
1.3
1.4
1.5
1.6
1.7
1.8
1.9
2.0
```

결과가 정확하게 잘 나오는 것을 볼 수 있습니다. 

<br>

### `BigDecimal 조심해야 할 것`

```java
import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        BigDecimal value = new BigDecimal("1.0");
        BigDecimal addValue = new BigDecimal(0.1);   // double 형 매개변수 생성자 사용
        for (int i = 0; i < 10; ++i) {
            value = value.add(addValue);
            System.out.println(value.toString());
        }
    }
}
```

위와 같이 String 매개변수 생성자를 사용하지 않고 double형 매개변수 생성자를 사용하면 아래와 같이 결과가 다르게 나옵니다. 

```
1.1000000000000000055511151231257827021181583404541015625
1.2000000000000000111022302462515654042363166809082031250
1.3000000000000000166533453693773481063544750213623046875
1.4000000000000000222044604925031308084726333618164062500
1.5000000000000000277555756156289135105907917022705078125
1.6000000000000000333066907387546962127089500427246093750
1.7000000000000000388578058618804789148271083831787109375
1.8000000000000000444089209850062616169452667236328125000
1.9000000000000000499600361081320443190634250640869140625
2.0000000000000000555111512312578270211815834045410156250
```

<br>

## `그러면 double, float을 쓰지 말고 BigDecimal만 쓰면 되지 않나?`

float은 7자리, double은 15자리 까지 자릿수 정밀도를 체크할 수 있습니다. 따라서 이정도 범위 내에서 계산을 해야한다면 new를 이용해 BigDecimal의 메모리를 할당하는 것 보다는
기본 자료형을 사용하는 것이 좋습니다. 

하지만 float, double로는 한계가 있다면 그 때 BigDecimal을 사용하면 됩니다. 