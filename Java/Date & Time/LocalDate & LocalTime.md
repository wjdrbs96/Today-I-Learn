# `들어가기 전에`

Java의 탄생부터 지금까지 날짜와 시간을 다루는데 사용해왔던, `Date`, `Calendar`는 문제가 많다고 저번에 정리했습니다. 그래서 이러한 단점들을 해소하기 위해
jdk 1.8부터 `java.time 패키지`가 추가되었습니다. 

`java.time` 패키지와 이 하위 패키지들의 가장 큰 특징은 `String 객체처럼 불변(immutable)이라는 것`입니다. 날짜와 시간을 변경하는 메소드들은 기존의 객체를 변경하는 대신 항상 변경된 새로운 객체를 반환합니다. 

기존의 Calendar 클래스는 변경이 가능해서 `Thread-safe 하지 못하다`는 단점을 가지고 있었는데 `java.time` 패키지 클래들은 `Thread-safe 하다는` 장점을 가지게 되었습니다. 

하지만 새로운 패키지가 도입되었음에도, 하위 클래스와의 호환성을 유지하기 위해서 `Date`, `Calendar` 클래스는 여전히 사용되고 있습니다. 

<br>

# `java.time 패키지의 핵심 클래스`

날짜와 시간을 하나로 표현하는 Calendar 클래스와는 달리, java.time 패키지에서는 날짜와 시간을 별도의 클래스로 분리해 놓았습니다. 

- `시간: LocalTime 클래스`
- `날짜: LocalDate 클래스`
- `시간 + 날짜: LocalDateTime 클래스`
- `LocalDateTime + 시간대: ZonedDateTime 클래스`

<br>

## `객체 생성하기 - now(), of()`

`java.time` 패키지에 속한 클래스의 객체를 생성하는 가장 기본적인 방법은 `now()`와 `of()`를 사용하는 것입니다. 

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class Test {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();                  // 2021-02-05
        LocalTime time = LocalTime.now();                  // 13:59:09.559
        LocalDateTime dateTime  = LocalDateTime.now();     // 2021-02-05T13:59:37.534
        ZonedDateTime datTimeInKr = ZonedDateTime.now();   // 2021-02-05T14:00:14.068+09:00[Asia/Seoul]
    }
}
```

`now()`는 위와 같이 현재 날짜와 시간을 저장하는 객체를 생성합니다. 

<br>

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Test {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2015, 11, 23);     // 2015-11-23
        LocalTime time = LocalTime.of(23, 59, 59);          // 23:59:59
        LocalDateTime dateTime = LocalDateTime.of(date, time);                    // 2015-11-23T23:59:59
    }
}
```

of()는 단순히 해당 필드의 값을 순서대로 지정해 주기만 하면 됩니다.