# `아이템50 : 적시에 방어적 복사본을 만드라`

> `클라이언트가 여러분의 불변식을 깨뜨리려 혈안이 되어 있다고 가정하고 방어적으로 프로그래밍 해야 합니다.`

어떤 객체든 그 객체의 허락 없이는 외부에서 내부를 수정하는 일은 불가능합니다. 

`하지만` 주의를 기울이지 않으면 자기도 모르게 내부를 수정하도록 허락하는 경우가 생깁니다. 


### 예를들어, 기간(period)를 표현하는 클래스를 보겠습니다.

```java
import java.util.Date;

public final class Period {
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(
                    start + "가 " + end + "보다 늦다.");
        }
        this.start = start;
        this.end = end;
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }
}
```

얼핏 보면 이 클래스는 불변으로 봉고, 시작 시각이 종료 시각보다 늦을 수 없다는 불변식이 문제 없이 지켜질 것 같습니다. 

`하지만 Date 클래스는 가변이기 때문에 아래와 같이 Period 클래스의 불변을 깨뜨릴 수 있습니다.`

```java
import java.util.Date;

public final class Period {
    public static void main(String[] args) {
        Date start = new Date();
        Date end = new Date();
        
        Period p = new Period(start, end);
        end.setYear(78);
    }
}
```

- Date는 낡은 API이니 새로운 코드를 작성할 때는 더 이상 사용하면 안됩니다.

<br>

> `외부 공격으로부터 Period 인스턴스의 내부를 보호하려면 생성자에서 받은 가변 매게변수 각각을 방어적으로 복사(defensive copy) 해야 한다.`

```java
import java.util.Date;

public final class Period {
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(
               start + "가 " + end + "보다 늦다.");
        }
    }
}
```

위와 같이 Period 인스턴스 안에서는 원본이 아닌 복사본을 사용하는 방법이 있습니다. 

- 새로 작성한 생성자로 더 이상 Period에 위협이 되지 않습니다. 
- 매개변수에 유효성을 검사하기 전에 방어적 복사본을 만들고, 이 복사본으로 유효성을 검사한 점에 주목해야 합니다.


### `순서가 부자연스러워 보여도 반드시 이렇게 작성해야 합니다.`

왜 그럴까요 ? ? 

- 멀티 스레딩 환경에서 문제 (이러한 공격을 TOCTOU 공격이라 한다네용..)

### `방어적 복사에 Date의 clone 메소드를 사용하지 않은 점에도 주목해야 합니다.`

- Date가 final이 아니라 clone 메소드를 호출하면 Date의 clone이 아니라 하위 클래스의 clone이 호출될 수 있습니다.
- `매게변수가 제 3자에 의해 확장될 수 있는 타입이라면 방어적 본사본을 만들 대 clone을 사용해서는 안됩니다.`

<br>

### `두 번째 공격`

```java
public final class Period {
    public static void main(String[] args) {
        Date start = new Date();
        Date end = new Date();

        Period p = new Period(start, end);
        p.end.setYear(78);
    }
}
```

이러한 공격을 방어하려면 아래와 같이 `가변 필드의 방어적 본사본`을 반환하면 됩니다.

```java
public final class Period {
    public Date start() {
        return new Date(start.getTime());
    }

    public Date end() {
        return new Date(end.getTime());
    }
}
```

<br>

## `핵심 정리`

- 클래스가 불변이든 가변이든 가변인 내부 객체를 클라에게 반환할 때 심사숙고 하기 (안심할 수 없으면 방어적 복사본 반환)
- 되도록 불변 객체들을 조합해 객체를 구성하기
- 복사 비용이 크거나 클라가 잘못 수정할 일이 없다고 신뢰한다면 방어적 복사대신 해당 요소를 수정했을 때 책임이 클라에게 있음을 문서에 명시하기 

<br>

- P304 아래 부분, P305 중간 문단, P306(통제권 이전 용어에 대하여), 
