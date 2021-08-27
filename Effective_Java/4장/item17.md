# `아이템17 : 변경 가능성을 최소화하라`

## `불변 클래스란?`

인스턴스의 내부 값을 수정할 수 없는 클래스를 의미합니다. 예를들면, `String`, `기본타입의 박싱 클래스들`, `BigInteger`, `BigDecimal`이 있습니다. 

이 클래스들을 불변으로 설계한 데는 그럴만한 이유가 있습니다. `불변 클래스는 가변 클래스보다 설계하고 구현하고 사용하기 쉬우며, 오류가 생길 여지도 적고 훨씬 안전하기 때문입니다.`

<br>

## `클래스를 불변으로 만들려면 다음 다섯 가지 규칙을 따르면 됩니다.`

- 객체의 상태를 변경하는 매소드를 제공하지 않는다.
- 클래스를 확장할 수 없도록 한다. 
    - 하위 클래스에서 좋지 않은 의도로 객체의 상태를 변하게 만들 수 있다.(final 키워드를 통해 상속을 막는 방법이 있습니다.)

-  모든 필드를 final로 선언한다. 
    - final 키워드를 통해 상수로 만들어서 새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 보장할 수 있습니다. 
    
- 모든 필드를 private로 선언한다. 
    - 필드가 참조하는 가변 객체를 클라이언트에서 직접 접근해 수정하는 일을 막아줍니다. 
 
- 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.
    - 클래스에 가변 객체를 참조하는 필드가 하나라도 있다면 클라이언트에서 그 객체의 참조를 얻을 수 없도록 해야 한다.
    

<br>

### `불변 객체는 단순하다.`

불변 객체는 모든 생성자가 클래스 불변식을 보장한다면 그 클래스를 사용하는 프로그래머가 다른 노력을 들이지 않더라도 영원히 불변으로 남는다.

<br.

### `불변 객체는 근본적으로 스레드 안전하여 따로 동기화할 필요 없다.`

여러 스레드가 동시에 사용해도 절대 훼손되지 않습니다. 클래스를 스레드 안전하게 만드는 가장 쉬운 방법입니다. 
불변 객체에 대해서는 그 어떤 스레드도 다른 스레드에 영향을 줄 수 없으니 `불변 객체는 안심하고 공유할 수 있습니다.`

`따라서 불변 클래스라면 한번 만든 인스턴스를 최대한 재활용하기를 권합니다.` 가장 쉬운 예로는 `public static final`로 제공하는 것입니다. 

이 방식으로 좀 더 자세히 살펴보겠습니다. `불변 클래스는 자주 사용되는 인스턴스를 캐싱하여 같은 인스턴스를 중복 생성하지 않게 해주는 정적 팩토리를 제공할 수 있습니다.`

박싱된 기본 타입 클래스 전부와 BigInteger가 여기 속합니다. 이런 정적 팩토리를 사용하면 여러 클라이언트가 인스턴스를 공유하여 메모리 사용량과 가비지 컬렉션 비용이 줄어듭니다. 

<br>

### `불변 객체는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다.`

예를들면 BigInteger 클래스는 내부에서 값의 부호와 크기를 따로 표현합니다. 부호에는 int 변수를, 크기(절대값)에는 int 배열을 사용합니다.

```java
public class BigInteger extends Number implements Comparable<BigInteger> {
    
    final int[] mag;
    final int signum;

    BigInteger(int[] magnitude, int signum) {
        this.signum = (magnitude.length == 0 ? 0 : signum);
        this.mag = magnitude;
        if (mag.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
    }

    public BigInteger negate() {
        return new BigInteger(this.mag, -this.signum);
    }
}
```

BigInteger 클래스에 `negate()` 메소드를 보면 크기가 같고 부호만 반대인 새로운 BigInteger를 생성하는데, 이 때 배열은 비록 가변이지만 복사하지 않고 원본 인스턴스와 공유해도 됩니다. 

<br>

### `객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다.`

값이 바뀌지 않는 구성요소들로 이뤄진 객체라면 그 구조가 아무리 복ㅈ바하더라도 불변식을 유지하기 훨씬 수월합니다.

<br>

### `불변 객체는 그 자체로 실패 원자성을 제공한다.`

- 상태가 절대 변하지 않으니 잠깐이라도 불일치 상태에 빠질 가능성이 없습니다.

<br>

### `불변 클래스에도 단점은 있다. 값이 다르면 반드시 독립된 객체로 만들어야 한다는 것이다.`

예를들어, 백만 비트짜리 BigInteger에서 비트 하나를 바꿔야 한다고 생각해보겠습니다. 이 때 BigInteger 클래스의 flitBit() 메소드는 새로운 BigInteger 인스턴스를 생성합니다. 
`원본과 단지 한 비트만 다른 백만 비트짜리 인스턴스를 말입니다.`

```java
public class BigInteger extends Number implements Comparable<BigInteger> {

    public BigInteger flipBit(int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit address");

        int intNum = n >>> 5;
        int[] result = new int[Math.max(intLength(), intNum+2)];

        for (int i=0; i < result.length; i++)
            result[result.length-i-1] = getInt(i);

        result[result.length-intNum-1] ^= (1 << (n & 31));

        return valueOf(result);
    }

}
```

이 메소드의 연산은 크기에 비레합니다. 따라서 시간 복잡도는 O(N) 입니다. 

<br>

```java
public class BitSet implements Cloneable, java.io.Serializable {
    
    public void flip(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);

        int wordIndex = wordIndex(bitIndex);
        expandTo(wordIndex);

        words[wordIndex] ^= (1L << bitIndex);

        recalculateWordsInUse();
        checkInvariants();
    }
}
```

위의 `flip` 메소드는 BitSet 클래스의 메소드인데 이것은 원하는 비트 하나만 바꿔주기 때문에 시간복잡도 O(1)이 걸리게 됩니다.

<br>

> P 110 문단 하나 정리 필요 -----


<br>

## `불변 클래스를 만드는 방법`

클래스가 불변임을 보장하려면 자신을 상속하지 못하게 해야 합니다. 그 방법에는 `final 클래스`를 만들거나 `모든 생성자를 private`로 만드는 것입니다. 

좀 더 유연한 방법은 생성자를 private로 지정하고 public 정적 팩토리를 제공하는 방법입니다. 

```java
public class Complex {
    private final double re;
    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }
    
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }
}
```

패키지 바깥의 클라이언트에서 바라본 이 불변 객체는 사실상 final 입니다. 생성자가 private 이기 때문에 상속이 불가능하고, 정적 팩토리 메소드로 객체를 제공하기 때문에
유연성을 제공할 수 있습니다.

<br>

## `정리`

- getter가 있다고 해서 무조건 setter를 만드는 것은 비추천입니다.
- 클래스는 꼭 필요한 경우가 아니라면 불변이어야 합니다.
- 불변 클래스는 장점이 많으면, 단점이라고는 특정 상황에서의 잠재적 성능 저하뿐입니다. 
- 단순한 값 객체는 항상 불변으로 만드는 것을 추천합니다.
- `불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.`
    - 객체를 가질 수 있는 상태를 줄이면 그 객체를 예측하기 쉬워지고 오류가 생길 가능성이 줄어듭니다.
- `다른 합당한 이유가 없다면 모든 필드는 private final이어야 합니다.`
- `생성자는 불변식 설정이 모두 완료된, 초기화가 완벽히 끝난 상태의 객체를 생성해야 합니다.`
    - 확실한 이유가 없다면 생성자와 정적 팩토리 외에는 그 어떤 초기화 메소드도 public으로 제공해서는 안됩니다.