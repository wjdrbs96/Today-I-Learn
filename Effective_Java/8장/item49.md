# `아이템49 : 매게변수가 유효한지 검사하라`

매게변수 검사를 제대로 하지 못하면 몇 가지 문제가 생길 수 있습니다. 

- 메소드가 수행되는 중간에 모호한 예외를 던지며 실패할 수 있다. 
    - 더 나쁜 상황은 메소드가 잘 실행되지만 잘못된 결과를 반환할 때입니다. 

public과 protected 메소드는 매게변수 값이 잘못됐을 때 던지는 예외를 문서화해야 합니다. 

```java
public class BigInteger {
    /**
     * Returns a BigInteger whose value is {@code (this mod m}).  This method
     * differs from {@code remainder} in that it always returns a
     * <i>non-negative</i> BigInteger.
     *
     * @param  m the modulus.
     * @return {@code this mod m}
     * @throws ArithmeticException {@code m} &le; 0
     * @see    #remainder
     */
    public BigInteger mod(BigInteger m) {
        if (m.signum <= 0)
            throw new ArithmeticException("BigInteger: modulus not positive");

        BigInteger result = this.remainder(m);
        return (result.signum >= 0 ? result : result.add(m));
    }
}
```

<br>

### `java.util.Objects.requireNonNull로 null 체크하기`

```
this.strategy = Objects.requireNonNull(strategy, "전략");  
```

자바는 null 검사를 위와 같이 할 수 있습니다.

<br>

### `public이 아닌 메소드라면 단언문(assert)을 사용해 매게변수 유효성을 검증하기`

- 첫 번째: 실패하면 AssertionError를 던진다. (제가 해보니 에러 발생이 안되던데 왜일까요..)
- 두 번째: 런타임에 아무런 효과도, 성능 저하도 없다. 

<br>

### `생성자의 매게변수 검증하기`

생성자 매게변수의 유효성 검사는 클래스 불변식을 어기는 객체가 만들어지지 않게 하는 데 꼭 필요합니다. 

<br>

### `핵심 정리`

- 메소드는 최대한 범용적으로 설꼐해야 합니다. 
- 메소드가 생성자를 작성할 때면 그 매게변수들에 어떤 제약이 있을지 생각해야 합니다.
- 그 제약들을 문서화하고 메소드 코드 시작 부분에서 명시적으로 검사해야 합니다. 



